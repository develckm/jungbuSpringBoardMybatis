const replyInserForm=document.forms["replyInserForm"];
const replyList=document.getElementById("replyList");

if(replyInserForm!=null){
	replyInserForm.addEventListener("submit",async(e)=>{
		e.preventDefault();
		let boardNo=replyInserForm.boardNo.value;
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
				case 1: 
					msg="등록성공"; 
					readReplyList(boardNo);
					break;
				case -1: msg="로그인을 하세요."; break;
			}
			alert(msg);
		}
	});
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
		console.log(textHtml);
		replyLiNode.innerHTML=textHtml;
	}else if(resp.status==400){
		alert("로그인한 유저만 수정 가능");
	}else if(resp.status==401){
		alert("글쓴 유저만 수정 가능");
	}else if(reps.status==500){
		alert("수정 폼 불러오기 실패(db 에러)");
	}
}




