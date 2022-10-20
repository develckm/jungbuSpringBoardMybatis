userInsertForm.userId.addEventListener("change",checkUserId)

async function checkUserId(){
	let userId=userInsertForm.userId.value;
	let url="/user/checkUserId.do?userId="+userId;
	let resp=await fetch(url)
	console.log(resp)
}