package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceimpl;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase vo = new Purchase();
		User user = new User();
		Product prod = new Product();
		
		user.setUserId(request.getParameter("buyerId"));
		user.setUserName(request.getParameter("receiverName"));
		user.setPhone(request.getParameter("receiverPhone"));
		
		vo.setBuyer(user);
		vo.setPurchaseProd(prod);
		vo.setPaymentOption(request.getParameter("paymentOption"));
		vo.setDivyAddr(request.getParameter("receiverAddr"));
		vo.setDivyRequest(request.getParameter("readonly"));
		
		PurchaseService service = new PurchaseServiceimpl();
		service.updatePurchase(vo);
		
		request.setAttribute("updateVO", vo);
		
		return "forward:/purchase/updatePurchaseReadView.jsp";
	}
}
