package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;

public class PurchaseDAO {

	public PurchaseDAO() {
	}

	public void insertPurchase(Purchase purchase) throws SQLException {

		Connection con = DBUtil.getConnection();

		System.out.println("DB접속");
		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL, ?, ?, ?, ?, ?, ?, ? ,? ,? ,?)";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		pstmt.setString(2, purchase.getBuyer().getUserId());
		pstmt.setString(3, purchase.getPaymentOption());
		pstmt.setString(4, purchase.getBuyer().getUserName());
		pstmt.setString(5, purchase.getBuyer().getPhone());
		pstmt.setString(6, purchase.getDivyAddr());
		pstmt.setString(7, purchase.getDivyRequest());
		pstmt.setString(8, purchase.getTranCode());
		pstmt.setDate(9, purchase.getOrderDate());
		pstmt.setString(10, purchase.getDivyDate());
		pstmt.executeUpdate();
		
		con.close();
		
		System.out.println("저장 완료");
	}

	public Purchase findPurchase(int no) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE prod_no = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, no);

		ResultSet rs = pstmt.executeQuery();

		Purchase purchase = null;
		
		while (rs.next()) {
			Product vo = new Product();
			User user = new User();
			
			vo.setProdNo(rs.getInt("prod_no"));
			user.setUserId(rs.getString("buyer_id"));
			user.setUserName(rs.getString("receiver_name"));
			user.setPhone(rs.getString("receiver_phone"));
			
			
			purchase = new Purchase();
			purchase.setBuyer(user);
			purchase.setPurchaseProd(vo);
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setDivyAddr(rs.getString("demailaddr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setOrderDate(rs.getDate("order_data"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			purchase.setTranCode(rs.getString("tran_status_code"));
			purchase.setTranNo(rs.getInt("tran_no"));

		}

		con.close();

		return purchase;
	}

	public Map<String, Object> getPurchaseList(Search search) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();

		Connection con = DBUtil.getConnection();

		System.out.println("DB시작");

		String sql = "SELECT tran_no, prod_no, buyer_id, payment_option, receiver_name, receiver_phone, demailaddr, dlvy_request, tran_status_code, order_data, dlvy_date FROM transaction t ORDER BY tran_no";
		
		System.out.println("오리지날 SQL문 : "+sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("TotalCount : "+totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement pstmt = con.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();
		
		System.out.println("ResultSet 완료");
		
		List<Purchase> list = new ArrayList<Purchase>();

		while(rs.next()) {
			Purchase vo = new Purchase();
			Product prod = new Product();
			User user = new User();
			
			user.setUserId(rs.getString("buyer_id"));
			prod.setProdNo(rs.getInt("prod_no"));
			
			vo.setBuyer(user);
			vo.setPurchaseProd(prod);
			vo.setTranNo(rs.getInt("tran_no"));
			vo.setPaymentOption(rs.getString("payment_option"));
			vo.setReceiverName(rs.getString("receiver_name"));
			vo.setReceiverPhone(rs.getString("receiver_phone"));
			vo.setDivyAddr(rs.getString("demailaddr"));
			vo.setDivyRequest(rs.getString("dlvy_request"));
			vo.setTranCode(rs.getString("tran_status_code"));
			vo.setOrderDate(rs.getDate("order_data"));
			vo.setDivyDate(rs.getString("dlvy_date"));
			
			list.add(vo);
			
		}
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
		
		System.out.println("list = "+map.get("list"));
		
		rs.close();
		pstmt.close();
		con.close();

		return map;
	}

	public void updatePurchase(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET buyer_id=?, payment_option=?, receiver_name=?, receiver_phone=?, demailaddr=?, dlvy_request=?, dlvy_date = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);
		
		pstmt.setString(1, purchase.getBuyer().getUserId());
		pstmt.setString(2, purchase.getPaymentOption());
		pstmt.setString(3, purchase.getBuyer().getUserName());
		pstmt.setString(4, purchase.getBuyer().getPhone());
		pstmt.setString(5, purchase.getDivyAddr());
		pstmt.setString(6, purchase.getDivyRequest());
		pstmt.setString(7, purchase.getDivyDate());

		int i = pstmt.executeUpdate();

		if (i == 1) {
			System.out.println("수정완료");
		} else {
			System.out.println("오류 발생");
		}
		con.close();
	}

	public void updateTranCode(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET tran_status_code=? WHERE prod_no = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, purchase.getTranCode());
		pstmt.setInt(2, purchase.getPurchaseProd().getProdNo());

		int i = pstmt.executeUpdate();

		if (i == 1) {
			System.out.println("수정완료");
		} else {
			System.out.println("오류 발생");
		}
		con.close();
	}
	
	private int getTotalCount(String sql) throws Exception{
		
		String sql1 = "SELECT COUNT(*) FROM(" +sql+") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql1);
		ResultSet rs = pstmt.executeQuery();
		
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt(1);
		}
		
		pstmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	private String makeCurrentPageSql(String sql, Search search) throws Exception{
		
		String sql1 = "SELECT * FROM ( SELECT inner_table.* , ROWNUM row_seq FROM ("+sql+") inner_table WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+") WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1)+" AND "+search.getCurrentPage()*search.getPageSize();
	
		System.out.println("추가된 SQL문 : "+sql1);
		
		return sql1;
	}
	
}
