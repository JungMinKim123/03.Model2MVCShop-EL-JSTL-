package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceimpl;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		
		String code = request.getParameter("tranCode");
		
		System.out.println("tranCode 값 : "+code);
		
		User vo = new User();
		Product prod = new Product();
		
		vo.setUserId(request.getParameter("buyerId"));
		vo.setUserName(request.getParameter("receiverName"));
		vo.setPhone(request.getParameter("receiverPhone"));
		
		prod.setProTranCode(code);
		prod.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		
		System.out.println("vo에 저장된 값 : "+vo);
		System.out.println("prod에 저장된 값 : "+prod);
		
		purchase.setPurchaseProd(prod);
		purchase.setBuyer(vo);
		purchase.setTranCode(code);
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("receiverDate"));
		purchase.setBuyer(vo);
		
		System.out.println("Request 받은 후 VO저장 값 : "+purchase);
		
		PurchaseService service = new PurchaseServiceimpl();
		service.addPurchase(purchase);

		System.out.println("DAO 저장 후 받은 후 VO저장 값 : "+purchase);
		
		request.setAttribute("addVO", purchase);
		
		return "forward:/purchase/addPurchaseReadView.jsp";
	}


}
