package bit.servlet;
   
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.ApplicationContext;

import bit.dao.MemberDao;
import bit.vo.Member;
  

@WebServlet("/member/detail.do")
public class MemberDetailServlet extends GenericServlet {
  private static final long serialVersionUID = 1L;

  MemberDao memberDao;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config); //원래 GenericServlet 만든 메서드를 그대로 실행한다.
 // 모든 서블릿이 공유하는 ServletContext 창고를 알아낸다.
    ServletContext servletContext = config.getServletContext();
    
    // 창고에 보관된 Spring IoC 컨테이너르 ㄹ꺼낸다.
    ApplicationContext applicationContext = 
        (ApplicationContext)servletContext.getAttribute("applicationContext");
    
    // Spring IoC 컨테이너에서 BoardDao 구현체를 꺼낸다.
    memberDao = applicationContext.getBean(MemberDao.class);
  }
  @Override
  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    int no = Integer.parseInt(request.getParameter("MNO"));
    
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    
    out.println("<html>");
    out.println("<head>");
    out.println("<title>게시물 목록조회</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>게시물 목록조회</h1>");
    
    try {
      Member member = memberDao.selectOne(no);
        if (member == null) {
          out.println("해당 번호의 게시물이 없습니다.");
        } else {
          out.printf("<form action='teacherupdate.do' method='post'>\n");
          out.printf("<input type='hidden' name='MNO'value='%d'>\n", member.getMNO());
          out.printf("번호: %d<br>\n", member.getMNO());
          out.printf("이름: <input type='text' name='MNM' value='%s'><br>\n", 
                    member.getMNM());
          out.printf("번호: <input type='text' name='TEL' value='%s'><br>\n",
              member.getTEL());
          out.printf("이메일: <input type='text' name='email' value='%s'><br>\n",
              member.getEmail());
          out.printf("<button>변경</button>");
          out.printf("<p><a href='teacherdelete.do?MNO=%d'>삭제</a></p>\n", member.getMNO());
          out.printf("</form>\n");
        }
     
    } catch (Exception e) {
      out.println("데이터 목록 조회 오류입니다.!");
      e.printStackTrace();
    }
    out.println("</body>");
    out.println("</html>");
    
  }
}


