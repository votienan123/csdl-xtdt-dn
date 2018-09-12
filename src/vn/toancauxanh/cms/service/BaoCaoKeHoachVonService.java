package vn.toancauxanh.cms.service;

import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.BaoCaoKeHoachVon;
import vn.toancauxanh.gg.model.QBaoCaoKeHoachVon;
import vn.toancauxanh.service.BasicService;

public class BaoCaoKeHoachVonService extends BasicService<BaoCaoKeHoachVon>{
		
	
	public JPAQuery<BaoCaoKeHoachVon> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");
				
		JPAQuery<BaoCaoKeHoachVon> phongBan = find(BaoCaoKeHoachVon.class)
				.where(QBaoCaoKeHoachVon.baoCaoKeHoachVon.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			phongBan.where(QBaoCaoKeHoachVon.baoCaoKeHoachVon.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			phongBan.where(QBaoCaoKeHoachVon.baoCaoKeHoachVon.trangThai.eq(trangThai));
		}
		phongBan.orderBy(QBaoCaoKeHoachVon.baoCaoKeHoachVon.ngaySua.desc());
		return phongBan;
	}
}
