userInsertForm.userId.addEventListener("change",checkUserId)

async function checkUserId(){
	userInsertForm.userId.classList.remove("is-invalid");
	userInsertForm.userId.classList.remove("is-valid");
	let userId=userInsertForm.userId.value;
	let url="/user/checkUserId.do?userId="+userId;
	if(userId.length>3){
		let resp=await fetch(url);
		if(resp.status==200){
			let json=await resp.json();		
			if(json.check==1){
				userIdInvalid.innerText="사용 중인 아이디입니다.";
				userInsertForm.userId.classList.add("is-invalid");
			}else if(json.check==0){
				userInsertForm.userId.classList.add("is-valid");
			}else if(json.check==-1){
				alert("db 조회 실패(다시시도)");
			}
		}else{
			alert("통신 장애(다시시도)"+resp.status);
		}
	}else{
		userIdInvalid.innerText="4글자 이상 작성하세요.";
		userInsertForm.userId.classList.add("is-invalid");
	}
}