$(document).ready(function () {
	// help 모달창 여닫는 이벤트
	$(document).on("click", "#helpBtn", function () {
		$("#helpModal").css("display", "flex");
	});
	$(document).on("click", "#helpModal .cancelBtn", function () {
		$("#helpModal").css("display", "none");
	});

	// 사용자 텍스트 박스 제출 이벤트
	function handleSendInput() {
		let $helpScreen = $("#helpScreen");
		if ($helpScreen.children().last().hasClass("userSection")) return;
		let inputText = $("#helpFooter .tb").val().trim();
		if (inputText !== "") {
			sendMessage(inputText);
			makeUserSection(inputText);
		}
	}

	$("#helpFooter .sendBtn").click(handleSendInput);
	$("#helpFooter .tb").keydown(function (e) {
		if (e.key === "Enter") handleSendInput();
	});


	// 메시지 배열 (시스템 프롬프트 포함)
	const defaultMessages = [{
		"role": "system", "content":
			`당신은 물고기 사전, 합사시뮬레이터 웹사이트 'Aqua Mate'의 전용 안내 챗봇입니다.
		아래의 사항을 반드시 엄격하게 지켜서 응답하세요.
		
		# 역할 및 제한사항 (절대 준수)
		- 당신은 'Aqua Mate' 웹사이트 이용 안내, 물고기 정보, 그리고 물고기 합사에 관련된 질문에만 답변할 수 있습니다.
		- 만약 사용자가 **물고기, 수족관, 합사, 혹은 Aqua Mate 웹사이트와 전혀 관련 없는 질문**(예: 코딩, 수학, 정치, 번역 등)을 할 경우, **절대로 해당 질문에 답변하지 말고** 오직 "저는 Aqua Mate와 물고기에 관련된 질문에만 답변할 수 있어요. 물고기나 사이트 이용에 대해 궁금한 점을 물어보세요!" 라고만 응답하세요.
		- 확실히 알지 못하는 정보는 지어내지 마세요.
		- 사용자의 인사에는 간단하게 인사로 화답하고 자기소개를 하세요.

		# 응답 방식
		- 줄바꿈과 이모티콘을 적절히 사용하여 친절하게 답변하고 가독성을 높이세요.
		
		# Aqua Mate 웹사이트 정보
		- Aqua Mate는 물고기 정보를 찾아볼 수 있는 '물고기 사전(Dictionary)'과, 두 마리의 물고기를 선택해 합사 궁합을 분석해주는 '합사 시뮬레이터(Match)' 기능을 제공합니다.
		- 로그인한 사용자는 물고기를 '즐겨찾기'할 수 있으며, 즐겨찾기한 물고기는 사전이나 매치 페이지 최상단에 정렬됩니다.
		- '마이페이지(My Page)'에서는 내 회원정보(이름, 이메일, 전화번호, 비밀번호)를 수정하거나 내가 즐겨찾기한 물고기들을 모아서 볼 수 있습니다.
		`
	}];

	let messages = JSON.parse(sessionStorage.getItem("chatMessages")) || defaultMessages;

	// 기존 메시지 화면에 렌더링
	messages.forEach(msg => {
		if (msg.role === "user") {
			makeUserSection(msg.content, true); // true 플래그로 입력창 비우기 생략
		} else if (msg.role === "assistant") {
			makeBotSection(msg.content);
		}
	});

	// 사용자 메시지 전송 메서드
	async function sendMessage(inputText) {
		// 텍스트 trim
		inputText = inputText.trim();
		if (!inputText) return;
		messages.push({ role: "user", content: inputText });
		sessionStorage.setItem("chatMessages", JSON.stringify(messages));
		
		const Maxmessages = 10;
		const recentMessages = [messages[0], ...messages.slice(-Maxmessages)];
		try {
			const response = await fetch("/api/chatbot/ask", {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify({
					model: "gpt-4o-mini",
					messages: recentMessages
				})
			});
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const data = await response.json();
			const botReply = data.choices[0].message.content;
			makeBotSection(botReply);
			messages.push({ role: "assistant", content: botReply });
			sessionStorage.setItem("chatMessages", JSON.stringify(messages));
		} catch (err) {
			console.error(err);
			makeBotSection("오류 발생 : " + err.message);
		}
	}



	// 사용자 텍스트 출력 메서드
	function makeUserSection(inputText) {
		let userSection = $("<div>", { class: "userSection" });
		let userIcon = $("<div>", { class: "userIcon" });
		let userText = $("<p>", { class: "userText", text: inputText });
		userSection.append(userIcon, userText);
		let $helpScreen = $("#helpScreen");
		$helpScreen.append(userSection);
		$("#helpFooter .tb").val("");
		// 채팅창 페이지 탑 변경
		$helpScreen.scrollTop($helpScreen[0].scrollHeight);
	}
	// 챗봇 텍스트 출력 메서드
	function makeBotSection(inputText) {
		let botSection = $("<div>", { class: "botSection" });
		let botIcon = $("<div>", { class: "botIcon" });
		let botText = $("<p>", { class: "botText", text: inputText });
		botSection.append(botIcon, botText);
		let $helpScreen = $("#helpScreen");
		$helpScreen.append(botSection);
		// 채팅창 페이지 탑 변경
		$helpScreen.scrollTop($helpScreen[0].scrollHeight);
	}

	// 모델창 내 토스트 메시지
	function toastMsg(message) {
		const msg = $("<div>", { text: message });
		msg.css("justify-self", "center");
		$("#helpScreen").append(msg);
		setTimeout(() => msg.fadeOut(500, () => msg.remove()), 800);
	}
});