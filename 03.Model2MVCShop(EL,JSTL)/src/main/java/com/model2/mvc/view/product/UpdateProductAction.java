package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceimpl;
import com.model2.mvc.service.domain.Product;

public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String prodNo1 = request.getParameter("prodNo");
		System.out.println("UpdateProductAction 시작 ");
		System.out.println("Request받은 prodNo : "+prodNo1);
		
		
		Product product = new Product();
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		System.out.println("VO저장 완료");
		
		ProductService service = new ProductServiceimpl();
		service.updateProduct(product);

		System.out.println("저장된 VO값 : "+product);
		
		request.setAttribute("update", product);
		
		return "forward:/product/updateReadProduct.jsp";
	}


}
