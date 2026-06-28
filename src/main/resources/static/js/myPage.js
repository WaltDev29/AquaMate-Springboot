$(document).ready(function () {
    // 물고기 데이터와 북마크 데이터를 불러오기
    Promise.all([
        fetch("/api/fish").then(res => res.json()),
        fetch("/api/user/bookmarks").then(res => {
            if (res.status === 401) return null;
            return res.json();
        }).catch(() => null)
    ])
    .then(([data, bookmarks]) => {
        if (!bookmarks) {
            // 미로그인 상태 (PageController에서 걸러지므로 사실상 도달 불가)
            return;
        }

        if (bookmarks.length === 0) {
            $("#no-bookmark-msg").show();
            return;
        }

        const htmlString = bookmarks.map(fishKey => {
            const fish = data[fishKey];
            if (!fish) return ''; // 혹시 삭제된 데이터면 무시

            const fontSize = fish.name.length >= 5 ? 'style="font-size: 16px;"' : '';
            return `
                <a href="/fish_info?fish=${fishKey}">
                    <article data-key="${fishKey}">
                        <div id="art_head">
                            <h3 ${fontSize}>${fish.name}</h3>
                            <div class="starBox active"></div>
                        </div>
                        <img src="/${fish.img}" alt="${fish.name}">
                    </article>
                </a>
            `;
        }).join('');

        $("#item_container").append(htmlString);
    })
    .catch(error => {
        console.error("데이터 불러오기 실패 : ", error);
        alert("정보를 불러오는 데 실패했습니다.");
    });

    // 즐겨찾기 해제 (API 연동)
    $(document).on("click", ".starBox", function (e) {
        e.stopPropagation();
        e.preventDefault();
        
        let $btn = $(this);
        let $article = $btn.closest("article");
        let fishKey = $article.attr("data-key");

        fetch(`/api/user/bookmarks/${fishKey}`, { method: 'DELETE' })
            .then(res => {
                if (res.ok) {
                    // 화면에서 제거
                    $article.parent("a").fadeOut(300, function() {
                        $(this).remove();
                        if ($("#item_container").children("a").length === 0) {
                            $("#no-bookmark-msg").show();
                        }
                    });
                } else {
                    alert("처리 중 오류가 발생했습니다.");
                }
            })
            .catch(err => console.error("Bookmark Error:", err));
    });
});
