const replyInserForm=document.forms["replyInserForm"];
const replyList=document.getElementById("replyList");

if(replyInserForm!=null){
	replyInserForm.addEventListener("submit",insertReply);
}
async function insertReply(e){
	e.preventDefault();
	const replyInserForm=e.target
	let boardNo=replyInserForm.boardNo.value;
	let url=replyInserForm.action;
	let data=new FormData(replyInserForm); 
	//multipart/form-data일대 파라미터를 blob으로 처리
	let resp=await fetch(url,{body:data,method:"post"});
	if(resp.status==200){//로그인이 안되어 있을때
		let json=await resp.json();
		let msg="";
		switch(json.status){
			case 0: msg="등록실패(db에러)"; break;
			case 1: 
				msg="등록성공"; 
				readReplyList(boardNo);
				break;
			case -1: msg="로그인을 하세요."; break;
		}
		alert(msg);
	}
}


async function readReplyList(boardNo,page=1){
	let url="/reply/"+boardNo+"/list.do?page="+page;
	let resp=await fetch(url);
	if(resp.status==200){
		let textHtml=await resp.text();
		replyList.innerHTML=textHtml;
	}else{
		alert("댓글 리스트 불러오기 실패!");
	}
}
async function readReplyUpdateForm(replyNo){
	let url="/reply/update.do?replyNo="+replyNo;
	const replyLiNode=document.getElementById("replyLi"+replyNo);
	let resp=await fetch(url);
	if(resp.status==200){
		let textHtml=await resp.text();
		replyLiNode.innerHTML=textHtml;
	}else if(resp.status==400){
		alert("로그인한 유저만 수정 가능");
	}else if(resp.status==401){
		alert("글쓴 유저만 수정 가능");
	}else if(reps.status==500){
		alert("수정 폼 불러오기 실패(db 에러)");
	}
}
async function deleteReply(replyNo,boardNo){
	let url="/reply/delete.do?replyNo="+replyNo;
	let resp=await fetch(url);
	let msg="";
	if(resp.status==200){
		let checkStatus=await resp.json();
		switch(checkStatus.status){
			case -2: msg="작성자만 삭제 가능"; break;
			case -1: msg="로그인 시 삭제 가능"; break;
			case 0: msg="삭제 실패 (db 오류)"; break;
			case 1: 
				msg="삭제 성공"; 
				readReplyList(boardNo);
			break;			
		}

	}else{
		msg="서버 통신 장애";
	}
	alert(msg);
}
async function updateReply(formNode){
	let url="/reply/update.do";
	let boardNo=formNode.boardNo.value;
	let data=new FormData(formNode);
	let resp=await fetch(url,{body:data,method:"post"});
	let msg="";
	if(resp.status==200){
		let checkStatus=await resp.json();
		switch(checkStatus.status){
			case -2: msg="작성자만 수정 가능"; break;
			case -1: msg="로그인 시 수정 가능"; break;
			case 0: msg="수정 실패 (db 오류)"; break;
			case 1: 
				msg="수정 성공"; 
				readReplyList(boardNo);
			break;			
		}
		
	}else{
		msg="수정 통신 실패"
	}
	alert(msg);
}



