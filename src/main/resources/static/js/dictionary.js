$(document).ready(function () {
    // 물고기 데이터 불러오기.
    const params = new URLSearchParams(window.location.search);

    fetch("/api/fish")
        .then(response => response.json())
        .then(data => {
            const fishKeys = Object.keys(data);

            // DOM 렌더링 최적화: 1회 순회 및 Template Literal 활용, 한 번에 append
            const htmlString = fishKeys.map(fishKey => {
                const fish = data[fishKey];
                const fontSize = fish.name.length >= 5 ? 'style="font-size: 16px;"' : '';
                return `
                    <a href="/fish_info?fish=${fishKey}">
                        <article>
                            <div id="art_head">
                                <h3 ${fontSize}>${fish.name}</h3>
                                <div class="starBox"></div>
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
        })

    // 즐겨찾기 버튼 클릭 이벤트 (CSS 클래스 토글 방식)
    $(document).on("click", ".starBox", function (e) {
        e.stopPropagation();
        e.preventDefault();
        $(this).toggleClass("active");
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
})