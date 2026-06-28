$(document).ready(function () {
    const params = new URLSearchParams(window.location.search);

    Promise.all([
        fetch("/api/fish").then(response => response.json()),
        fetch("/api/user/bookmarks").then(response => {
            if (response.status === 401) return [];
            return response.json();
        }).catch(() => [])
    ])
    .then(([data, currentBookmarks]) => {
        const fishKeys = Object.keys(data).sort((a, b) => {
            const aBookmarked = currentBookmarks.includes(a);
            const bBookmarked = currentBookmarks.includes(b);
            if (aBookmarked && !bBookmarked) return -1;
            if (!aBookmarked && bBookmarked) return 1;
            return 0;
        });

        // DOM 렌더링 최적화: 1회 순회 및 Template Literal 활용, 한 번에 append
        const htmlString = fishKeys.map(fishKey => {
            const fish = data[fishKey];
            const activeClass = currentBookmarks.includes(fishKey) ? "active" : "";
            return `
                <article class="aside_card" data-key="${fishKey}">
                    <img src="/${fish.img}" alt="${fish.name}">
                    <div class="aside_describe">
                        <a href="/fish_info?fish=${fishKey}">
                            <h4>${fish.name}</h4>
                        </a>
                        <p>바다 &nbsp;&nbsp;20~25C</p>
                    </div>
                    <div class="star_container">
                        <button class="starBox ${activeClass}"></button>
                    </div>
                </article>
            `;
        }).join('');
        
        $("#item_container").append(htmlString);
    })
    .catch(error => {
        console.error("JSON 불러오기 실패 : ", error);
        alert("물고기 정보를 불러오는 데 실패했습니다.");
    });

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
        
        // 물고기 변경 시 결과창 숨김 및 하트 초기화
        $("#result").css("display", "none").text("");
        $("#match_heart").text("💖");
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

    // 즐겨찾기 버튼 클릭 이벤트 (API 연동)
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

    // 궁합 확인 버튼 로직
    $("#btn_check_match").click(function() {
        let $matchCard0 = $(".match_card").eq(0);
        let $matchCard1 = $(".match_card").eq(1);
        let fish1 = $matchCard0.find("h3").text();
        let fish2 = $matchCard1.find("h3").text();

        let $btn = $(this);
        $btn.prop("disabled", true).text("궁합 분석 중...");
        $("#result").css("display", "block").html("AI가 열심히 분석하고 있어요... 🐟🔍");

        fetch("/api/chatbot/match", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fish1: fish1, fish2: fish2 })
        })
        .then(res => res.json())
        .then(data => {
            $btn.prop("disabled", false).text("궁합 확인하기");
            try {
                const resultJson = JSON.parse(data.choices[0].message.content);
                $("#result").html(`궁합: <span style="font-size: 1.3rem;">${resultJson.percentage}%</span><br><br><span style="color: #333; font-weight: normal; font-size: 1rem;">${resultJson.comment}</span>`);
                
                let emoji = "💔";
                if (resultJson.percentage >= 75) emoji = "💖";
                else if (resultJson.percentage >= 50) emoji = "💛";
                else if (resultJson.percentage >= 25) emoji = "❤️‍🩹";
                
                $("#match_heart").text(emoji);
            } catch(e) {
                $("#result").text("결과를 분석하는 중 오류가 발생했습니다.");
            }
        })
        .catch(err => {
            console.error(err);
            $btn.prop("disabled", false).text("궁합 확인하기");
            $("#result").text("통신 오류가 발생했습니다.");
        });
    });
});