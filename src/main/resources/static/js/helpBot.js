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
	let messages = [{"role": "system", "content": 
		`당신은 물고기 사전, 합사시뮬레이터 웹사이트 'Aqua Mate'의 안내 챗봇입니다.
		아래의 사항에 따라 사용자에게 응답하세요.
		# 역할
		- 알지 못하는 정보를 사용자에게 제공하지 않습니다.
		- 사용자가 'Aqua Mate'에 대한 질문을 할 경우 응답합니다.
		- 사용자가 물고기에 관련된 질문을 할 경우 응답합니다.
		- 물고기 합사에 관련된 질문을 할 경우 응답합니다.
		- 이외의 질문에는 '저는 Aqua Mate와 물고기에 관련된 응답만 할 수 있어요. 다른 질문을 해주세요.'를 출력합니다.

		# 응답 방식
		- 줄바꿈을 사용하여 가독성을 높이세요.
		
		# Aqua Mate
		- Aqua Mate는 물고기 사전, 합사 시뮬레이터로 이루어져 있습니다.
		- 연습용 프로젝트 웹사이트이기 때문에 제대로 기능하지 않습니다.				
		`
	}];

	// 사용자 메시지 전송 메서드
	async function sendMessage(inputText) {
		// 텍스트 trim
		inputText = inputText.trim();
		if (!inputText) return;
		messages.push({ role: "user", content: inputText });
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