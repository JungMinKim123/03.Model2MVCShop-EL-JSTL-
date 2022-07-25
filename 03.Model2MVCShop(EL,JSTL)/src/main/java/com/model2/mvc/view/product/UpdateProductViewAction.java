package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceimpl;
import com.model2.mvc.service.domain.Product;

public class UpdateProductViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ProdNo = request.getParameter("prodNo");
		
		ProductService service = new ProductServiceimpl();
		Product vo = service.getProduct(Integer.parseInt(ProdNo));
		
		request.setAttribute("ProdVO", vo);
		
		return "forward:/product/updateProduct.jsp";
	}


}
