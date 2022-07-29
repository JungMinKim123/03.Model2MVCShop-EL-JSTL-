package com.model2.mvc.service.product.dao;

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
//import com.model2.mvc.service.domain.vo.PurchaseVO;

public class ProductDAO {

	public ProductDAO() {
	}

	public void insertProduct(Product product) throws SQLException {

		Connection con = DBUtil.getConnection();

		System.out.println("DB접속");
		String spl = "INSERT INTO product VALUES ( seq_product_prod_no.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";

		System.out.println("작성한 SQL문 : "+spl);

		PreparedStatement pstmt = con.prepareStatement(spl);
		
		System.out.println("SQL문 등록");
		
		pstmt.setString(1, product.getProdName());
		pstmt.setString(2, product.getProdDetail());
		pstmt.setString(3, product.getManuDate());
		pstmt.setInt(4, product.getPrice());
		pstmt.setString(5, product.getFileName());
		pstmt.executeUpdate();
		
		System.out.println("Request 받은 후");
		
		pstmt.close();
		con.close();
	}

	public Product findProduct(int productNo) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM product WHERE PROD_NO = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, productNo);

		ResultSet rs = pstmt.executeQuery();

		Product product = null;

		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
		}
		
		pstmt.close();
		con.close();

		return product;
	}

	public Map<String, Object> getProductList(Search search) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		Connection con = DBUtil.getConnection();

		System.out.println("DB시작");

		String sql = "SELECT t.tran_status_code, p.prod_no, p.prod_name, p.prod_detail, p.manufacture_day, p.price, p.image_file, p.reg_date FROM product p, transaction t WHERE p.prod_no = t.prod_no(+) ";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")) {
				sql += "AND p.prod_no = " + search.getSearchKeyword() +"";
			} else if (search.getSearchCondition().equals("1")&& !search.getSearchKeyword().equals("")) {
				sql += "AND p.prod_name LIKE '" + search.getSearchKeyword() +"%"+"' ";
			} else if (search.getSearchCondition().equals("2")&& !search.getSearchKeyword().equals("")) {
				sql += "AND p.price=" + search.getSearchKeyword() + "";
			}
		}
		sql += " ORDER BY p.prod_no";

		System.out.println("오리지날 SQL 문 : " + sql);

		int totalCount = this.getTotalCount(sql);
		System.out.println("TotalCount : " + totalCount);

		sql = makeCurrentPageSql(sql, search);

		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		List<Product> list = new ArrayList<Product>();

		while (rs.next()) {
			Product vo = new Product();
			vo.setProdNo(rs.getInt("prod_no"));
			vo.setProdName(rs.getString("prod_name"));
			vo.setProdDetail(rs.getString("prod_detail"));
			vo.setManuDate(rs.getString("manufacture_day"));
			vo.setPrice(rs.getInt("price"));
			vo.setFileName(rs.getString("image_file"));
			vo.setProTranCode(rs.getString("tran_status_code"));

			list.add(vo);
		}

		map.put("totalCount", new Integer(totalCount));
		System.out.println("list.size() : " + list.size());
		map.put("list", list);
		System.out.println("list = " + map.get("list"));
		System.out.println("map().size() : " + map.size());
		
		rs.close();
		pstmt.close();
		con.close();

		return map;
	}

	public void updateProduct(Product product) throws Exception {

		Connection co = DBUtil.getConnection();

		String spl = "UPDATE product SET prod_name = ?, prod_detail = ?, manufacture_day = ?, price = ?, image_file = ? WHERE prod_no=?";

		PreparedStatement ps = co.prepareStatement(spl);

		ps.setString(1, product.getProdName());
		ps.setString(2, product.getProdDetail());
		ps.setString(3, product.getManuDate());
		ps.setInt(4, product.getPrice());
		ps.setString(5, product.getFileName());
		ps.setInt(6, product.getProdNo());
		ps.executeUpdate();
		
		ps.close();
		co.close();
	}

	private int getTotalCount(String sql) throws Exception {

		String sql1 = "SELECT COUNT(*) " + "FROM( " + sql + " ) countTable";

		Connection con = DBUtil.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql1);
		ResultSet rs = pstmt.executeQuery();

		int totalCount = 0;
		if (rs.next()) {
			totalCount = rs.getInt(1);
		}

		pstmt.close();
		con.close();
		rs.close();

		return totalCount;
	}

	private String makeCurrentPageSql(String sql, Search search) {

		String sql1 ="SELECT * FROM ( SELECT inner_table.* ,  ROWNUM AS row_seq FROM (	"+sql+" ) inner_table WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();

		System.out.println("추가된 SQL문" + sql1);
		
		return sql1;
	}
}
