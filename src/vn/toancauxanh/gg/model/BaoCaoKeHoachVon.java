package vn.toancauxanh.gg.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.toancauxanh.gg.model.enums.TrangThaiKeHoachVonEnum;
import vn.toancauxanh.model.Model;

@Entity
@Table(name = "baocaokehoachvon", indexes = { @Index(columnList = "ten")})
public class BaoCaoKeHoachVon extends Model<BaoCaoKeHoachVon>{
	
	private String ten = "";
	private String moTa = "";
	private int nam;
	
	public String getTen() {
		return ten;
	}
	
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
	public int getNam() {
		return nam;
	}

	public void setNam(int nam) {
		this.nam = nam;
	}
	
	@Transient
	public List<KeHoachNhuCauVon> getListKeHoach() {
		List<KeHoachNhuCauVon> list = new ArrayList<KeHoachNhuCauVon>();
		list = find(KeHoachNhuCauVon.class)
				.where(QKeHoachNhuCauVon.keHoachNhuCauVon.trangThaiKeHoach.eq(TrangThaiKeHoachVonEnum.DA_DUYET))
				.where(QKeHoachNhuCauVon.keHoachNhuCauVon.nam.eq(getNam()))
				.fetch();
		return list;
	}

	@Command
	public void saveBaoCaoKeHoachVon(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr, 
			@BindingParam("pheDuyet") final boolean pheDuyet,
			@BindingParam("wdn") final Window wdn) throws IOException {	
		
		save();
		
		
		if (getCurrentTask() != null) {
			System.out.println("getCurrentTask(): " + getCurrentTask().getName());
			Map<String, Object> variables = new HashMap<>();
			variables.put("pheDuyet", pheDuyet);
			core().getProcess().getTaskService().complete(getCurrentTask().getId(), variables);
		}
		
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);	
	}
}
