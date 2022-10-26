const replyInserForm=document.forms["replyInserForm"];
if(replyInserForm!=null){
	replyInserForm.addEventListener("submit",async(e)=>{
		e.preventDefault();
		let url=replyInserForm.action;
		let data=new FormData(replyInserForm); 
		//multipart/form-data일대 파라미터를 blob으로 처리
		let resp=await fetch(url,{body:data,method:"post"});
		console.log(resp)
		if(resp.status==200){//로그인이 안되어 있을때
			let json=await resp.json();
			let msg="";
			switch(json.status){
				case 0: msg="등록실패(db에러)"; break;
				case 1: msg="등록성공"; break;
				case -1: msg="로그인을 하세요."; break;
			}
			alert(msg);
		}
	});
}
readReplyList(23);
async function readReplyList(boardNo,page=1){
	let url="/reply/"+boardNo+"/list.do?page="+page;
	let resp=await fetch(url);
	let textHtml=await resp.text();
	console.log(textHtml);
}






