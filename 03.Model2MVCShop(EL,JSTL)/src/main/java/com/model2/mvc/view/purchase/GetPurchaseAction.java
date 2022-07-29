package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceimpl;
import com.model2.mvc.service.domain.Purchase;

public class GetPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tranNo = request.getParameter("tranNo");
		
		System.out.println("tranNo 값 : "+tranNo);
		
		PurchaseService service = new PurchaseServiceimpl();
		Purchase purVo = service.getPurchase(Integer.parseInt(tranNo));
		
		request.setAttribute("purVo", purVo);
		
		System.out.println("find해온 값 : "+purVo);
		
		return "forward:/purchase/purchaseReadView.jsp";
	}


}
