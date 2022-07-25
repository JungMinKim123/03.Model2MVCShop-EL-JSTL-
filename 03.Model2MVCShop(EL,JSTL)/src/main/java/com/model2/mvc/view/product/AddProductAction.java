package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceimpl;
import com.model2.mvc.service.domain.Product;

public class AddProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Product product = new Product();
		
		String md = request.getParameter("manuDate"); 
		String[] manu = md.split("-");
		String manudate = manu[0]+manu[1]+manu[2];
		
		System.out.println(manudate);
		
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(manudate);
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		
		
		System.out.println("Request 받고 VO에 저장한 내용 : "+product);
		
		ProductService service = new ProductServiceimpl();
		service.addProduct(product);
		
		request.setAttribute("ProdVO", product);
		
		return "forward:/product/readProductView.jsp"; 
	}

}
