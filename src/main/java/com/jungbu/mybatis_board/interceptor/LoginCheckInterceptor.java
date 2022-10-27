package com.jungbu.mybatis_board.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
//LoginCheckInterceptor를 작성하고 config를 정의해야 사용 가능
@Component
public class LoginCheckInterceptor implements HandlerInterceptor{
	//Filter(톰캣) 는 servlet에 요청하기전에 제어하기 위해 존재 (==preHandle)
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		//contoller에 요청하기 전에
		HttpSession session=request.getSession();
		Object loginUser_obj=session.getAttribute("loginUser");
		if(loginUser_obj!=null) {
			return true;	//==filter.doChain(req,resp)		
		}else {
			String uri=request.getRequestURI();
			System.out.println("LoginCheckInterceptor / "+uri+"가기 전(preHadler)");
			session.setAttribute("msg", "로그인 후 이용하세요!");
			response.sendRedirect("/user/login.do");
			return false;			
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
		//controller 요청이 완료된 후(Veiw를 요청하기 전)
		//String uri=request.getRequestURI();
		//System.out.println(uri+"처리 후(postHandle)");

	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)throws Exception {
		//view 처리가 완료된 후(Thymleaf html 파일이 실행되서 응답하기 전) 
		//String uri=request.getRequestURI();
		//System.out.println(uri+"View 처리 후(afterCompletion)");

	}
}
