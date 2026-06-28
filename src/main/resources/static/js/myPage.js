$(document).ready(function () {
    // 로컬 스토리지에서 북마크 불러오기 (배열 형태 예상)
    let bookmarks = JSON.parse(localStorage.getItem('bookmarks') || '[]');

    if (bookmarks.length === 0) {
        $("#no-bookmark-msg").show();
        return;
    }

    // 물고기 데이터 불러오기.
    fetch("/api/fish")
        .then(response => response.json())
        .then(data => {
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
            console.error("JSON 불러오기 실패 : ", error);
            alert("물고기 정보를 불러오는 데 실패했습니다.");
        });

    // 즐겨찾기 해제 (마이페이지에서 북마크 아이콘 클릭 시 로컬 스토리지 업데이트 및 DOM 제거)
    $(document).on("click", ".starBox", function (e) {
        e.stopPropagation();
        e.preventDefault();
        
        let $article = $(this).closest("article");
        let fishKey = $article.attr("data-key");

        // 로컬 스토리지 갱신
        let currentBookmarks = JSON.parse(localStorage.getItem('bookmarks') || '[]');
        currentBookmarks = currentBookmarks.filter(k => k !== fishKey);
        localStorage.setItem('bookmarks', JSON.stringify(currentBookmarks));

        // 화면에서 제거
        $article.parent("a").fadeOut(300, function() {
            $(this).remove();
            if (currentBookmarks.length === 0) {
                $("#no-bookmark-msg").show();
            }
        });
    });
});
