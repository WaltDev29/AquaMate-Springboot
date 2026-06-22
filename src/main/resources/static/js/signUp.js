$(document).ready(function () {
	let isValid = true;
	// jQuery 요소 캐싱
	const $tbInputs = $(".tb input");
	const $tbPw = $("#tb_pw");
	const $tbPwCheck = $("#tb_pwCheck");
	const $tbPhoneNum = $("#tb_phoneNum");

	// submit 유효성 검사
	$("#div_signUp").submit(function (e) {
		isValid = true;
		// border, placeholder 초기화
		$tbInputs.css("border", "2px solid rgb(116, 114, 114)").removeAttr("placeholder");

		// tb 공백 확인
		$tbInputs.each(function () {
			const $this = $(this);
			if ($this.val().trim() === "") {
				isValid = false;
				$this.css("border", "2px solid red").attr("placeholder", "정보를 입력해주세요");
			}
		});

		// 비밀번호 확인: 특수문자 최소 1개 포함, 영문/숫자/특수문자 조합 8~15자리 (한글 및 공백 제외)
		let pwInput = $tbPw.val().trim();
		let pwRegex = /^(?=.*[^a-zA-Z0-9])[a-zA-Z0-9!@#$%^&*()_+~\-={}[\]:;<>?,./]{8,15}$/;
		if (!pwRegex.test(pwInput)) {
			isValid = false;
			$tbPw.css("border", "2px solid red").val("").attr("placeholder", "특수문자 포함 8~15자리 입력");
		}

		// 비밀번호 일치 확인
		if (pwInput !== $tbPwCheck.val()) {
			isValid = false;
			$tbPwCheck.css("border", "2px solid red").val("").attr("placeholder", "비밀번호가 일치하지 않습니다.");
		}

		// 전화번호 확인
		let phoneRegex = /^(010-\d{4}-\d{4})$/;
		if (!phoneRegex.test($tbPhoneNum.val())) {
			isValid = false;
			$tbPhoneNum.val("").attr("placeholder", "맞지 않는 형식입니다.");
		}

		// 이벤트 막기
		if (!isValid) e.preventDefault();
	});

	// 텍스트를 입력하면 border 초기화
	$(".tb input").on("input", function () {
		if (!isValid) {
			if ($(this).val().trim() !== "") $(this).css("border", "2px solid rgb(116, 114, 114)");
			else $(this).css("border", "2px solid red");
		}
	})

	// 전화번호 문자 입력 제한 & 하이픈 자동 입력
	$("#tb_phoneNum").keydown(function (e) {
		let value = $(this).val();

		// 숫자 이외의 문자 입력 막기
		if ((e.key > '9' || e.key < '0') && e.key !== "Backspace") e.preventDefault();

		// 숫자, 하이픈 이외의 문자 지우기
		value = value.replace(/[^0-9\-]/g, "");

		// 하이픈 자동 입력
		if (e.key !== "Backspace") {
			if (value.length === 3 || value.length === 8) value += '-';
		}
		// 하이픈 자동 제거
		else if (value.length === 5 || value.length === 10) value = value.slice(0, -1);

		$(this).val(value);
	});

	// 한글 입력 제한
	$(".tb input:not(#tb_phoneNum, #tb_name)").on("input", function () {
		let value = $(this).val();
		let korean = /[가-힣ㄱ-ㅎㅏ-ㅣ]/g;
		value = value.replace(korean, "");
		$(this).val(value);
	});
})
