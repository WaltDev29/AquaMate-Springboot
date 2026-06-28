$(document).ready(function () {
    const params = new URLSearchParams(window.location.search);

    // 물고기 데이터와 즐겨찾기 데이터를 동시에 불러옴
    Promise.all([
        fetch("/api/fish").then(response => response.json()),
        fetch("/api/user/bookmarks").then(response => {
            if (response.status === 401) return [];
            return response.json();
        }).catch(() => [])
    ])
    .then(([data, currentBookmarks]) => {
        const fishKeys = Object.keys(data);

        // DOM 렌더링 최적화: 1회 순회 및 Template Literal 활용, 한 번에 append
        const htmlString = fishKeys.map(fishKey => {
            const fish = data[fishKey];
            const fontSize = fish.name.length >= 5 ? 'style="font-size: 16px;"' : '';
            const activeClass = currentBookmarks.includes(fishKey) ? "active" : "";
            return `
                <a href="/fish_info?fish=${fishKey}">
                    <article data-key="${fishKey}">
                        <div id="art_head">
                            <h3 ${fontSize}>${fish.name}</h3>
                            <div class="starBox ${activeClass}"></div>
                        </div>
                        <img src="/${fish.img}" alt="${fish.name}">
                    </article>
                </a>
            `;
        }).join('');

        $("#item_container").append(htmlString);
    })
    .catch(error => {
        console.error("JSON 불러오기 실패 : ", error);
        alert("물고기 정보를 불러오는 데 실패했습니다.");
    });

    // 즐겨찾기 버튼 클릭 이벤트 (API 통신)
    $(document).on("click", ".starBox", function (e) {
        e.stopPropagation();
        e.preventDefault();
        
        let $btn = $(this);
        let $article = $btn.closest("article");
        let fishKey = $article.attr("data-key");
        if (!fishKey) return;

        const isAdd = !$btn.hasClass("active");
        const method = isAdd ? "POST" : "DELETE";

        fetch(`/api/user/bookmarks/${fishKey}`, { method: method })
            .then(res => {
                if (res.status === 401) {
                    alert("로그인이 필요합니다.");
                } else if (res.ok) {
                    $btn.toggleClass("active");
                } else {
                    alert("처리 중 오류가 발생했습니다.");
                }
            })
            .catch(err => console.error("Bookmark Error:", err));
    });

    // 이름 검색 이벤트
    $("#btn_search").click(searchName)
    $("#tb_search").keydown(function (e) {
        if (e.key == "Enter") searchName();
    })
    // 이름 검색 함수
    function searchName() {
        let value = $("#tb_search").val().trim();
        $("article h3").each(function () {
            $(this).closest("article").css("display", "flex");
            if (!$(this).text().includes(value)) $(this).closest("article").css("display", "none");
        })
    }
});