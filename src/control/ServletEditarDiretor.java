package control;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Conexao;
import dao.DetalharDiretorDAO;
import dao.EditarDiretorDAO;
import dao.ListarFilmesDAO;
import model.Diretor;
import model.Filme;

/**
 * Servlet implementation class ServletEditarDiretor
 */
@WebServlet("/editarDiretor")
public class ServletEditarDiretor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletEditarDiretor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conexao = Conexao.getConexao();

		EditarDiretorDAO ead = new EditarDiretorDAO(conexao);
		
		String iddiretor = request.getParameter("iddiretor");
		
		DetalharDiretorDAO dad = new DetalharDiretorDAO(conexao);
		
		Diretor a = dad.getDiretor(iddiretor);
		
		String nome = request.getParameter("nome");
		
		if(nome.equals("")){
			nome = a.getNome();
		}
		
		String biografia = request.getParameter("biografia");
		
		if(biografia.equals("")){
			biografia = a.getBiografia();
		}
		
		String nascimento = request.getParameter("nascimento");
		
		java.sql.Date sqlDate = null;
		
		if(nascimento.equals("")){
			sqlDate = a.getData_nasc();
		}else{
			sqlDate = java.sql.Date.valueOf(nascimento);
		}
		
		String url = request.getParameter("url");
		
		if(url.equals("")){
			url = a.getUrl();
		}
		
		String i = request.getParameter("idfilme");
		int idfilme = Integer.parseInt(i);

		boolean resultado = ead.editarDiretor(nome,biografia,sqlDate,url, Integer.parseInt(iddiretor), idfilme);
		
		ListarFilmesDAO lfd = new ListarFilmesDAO(conexao);
		ArrayList<Filme> f = lfd.getTodosFilmes();
		
		if(resultado){
			request.setAttribute("idfilme", iddiretor);
			request.setAttribute("diretor", a);
			RequestDispatcher rd = request.getRequestDispatcher("/detalharDiretor");
			rd.forward(request, response);
		}else{
			request.setAttribute("mensagem", "Erro ao editar diretor, tente novamente!");
			request.setAttribute("diretor", a);
			request.setAttribute("filmes", f);
			RequestDispatcher rd = request.getRequestDispatcher("/respostaPaginaEditarDiretor.jsp");
			rd.forward(request, response);
		}
		
	}

}
