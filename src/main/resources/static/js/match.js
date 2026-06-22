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
                return `
                    <article class="aside_card">
                        <img src="/${fish.img}" alt="${fish.name}">
                        <div class="aside_describe">
                            <a href="/fish_info?fish=${fishKey}">
                                <h4>${fish.name}</h4>
                            </a>
                            <p>바다 &nbsp;&nbsp;20~25C</p>
                        </div>
                        <div class="star_container">
                            <button class="starBox"></button>
                        </div>
                    </article>
                `;
            }).join('');
            
            $("#item_container").append(htmlString);
        })
        .catch(error => {
            console.error("JSON 불러오기 실패 : ", error);
            alert("물고기 정보를 불러오는 데 실패했습니다.");
        })

    // 매치 카드 전환 (jQuery 캐싱 적용)
    $(document).on("click", ".aside_card", function () {
        let name = $(this).find("h4").text();
        let img = $(this).find("img").attr("src");
        let $matchCard0 = $(".match_card").eq(0);
        let $matchCard1 = $(".match_card").eq(1);

        if ($matchCard0.attr("data-changed") == "false") {
            $matchCard0.find("h3").text(name).css("font-size", name.length >= 5 ? "16px" : "");
            $matchCard0.find("img").attr("src", img);
            $matchCard0.attr("data-changed", "true");
        }
        else {
            $matchCard1.find("h3").text(name).css("font-size", name.length >= 5 ? "16px" : "");
            $matchCard1.find("img").attr("src", img);
            $matchCard1.attr("data-changed", "true");
            $matchCard0.attr("data-changed", "false");
        }
    });

    // 이름 검색 이벤트
    $(document).on("click", "#btn_search", searchName)
    $("#tb_search").keydown(function (e) {
        if (e.key == "Enter") searchName();
    })
    // 이름 검색 함수
    function searchName() {
        let value = $("#tb_search").val().trim();
        $(".aside_card h4").each(function () {
            $(this).closest("article").css("display", "flex");
            if (!$(this).text().includes(value)) $(this).closest("article").css("display", "none");
        })
    }

    // 즐겨찾기 버튼 클릭 이벤트 (CSS 클래스 토글 방식)
    $(document).on("click", ".starBox", function (e) {
        e.stopPropagation();
        e.preventDefault();
        $(this).toggleClass("active");
    });
})