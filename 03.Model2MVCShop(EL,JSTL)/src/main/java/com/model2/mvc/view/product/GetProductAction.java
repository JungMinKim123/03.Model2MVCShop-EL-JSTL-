package com.model2.mvc.view.product;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceimpl;
import com.model2.mvc.service.domain.Product;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String prodNo=request.getParameter("prodNo");
		
		ProductService service = new ProductServiceimpl();
		Product vo = service.getProduct(Integer.parseInt(prodNo));
		
		request.setAttribute("vo", vo);
		
		String history = "";
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("history")) {
					history = URLDecoder.decode(","+cookie.getValue());
				}
			}
		}
		
		Cookie cookie = new Cookie("history",URLEncoder.encode(prodNo+history));
		response.addCookie(cookie);
		
		return "forward:/product/updateProductView.jsp";
	}
}
