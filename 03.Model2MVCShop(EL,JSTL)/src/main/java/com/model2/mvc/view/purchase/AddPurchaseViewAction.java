package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceimpl;
import com.model2.mvc.service.domain.Product;

public class AddPurchaseViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("Action Ω√¿€");
		
		String prodNo = request.getParameter("prod_no");
		
		ProductService service = new ProductServiceimpl();
		Product vo = service.getProduct(Integer.parseInt(prodNo));
		
		request.setAttribute("addview", vo);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

}
