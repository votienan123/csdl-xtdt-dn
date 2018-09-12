package vn.toancauxanh.gg.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

import vn.toancauxanh.gg.model.enums.TrangThaiKeHoachVonEnum;
import vn.toancauxanh.model.Model;

@Entity
@Table(name = "kehoachnhucauvon", indexes = { @Index(columnList = "ten")})
public class KeHoachNhuCauVon extends Model<KeHoachNhuCauVon>{
	
	private String ten = "";
	private String moTa = "";
	private TrangThaiKeHoachVonEnum trangThaiKeHoach;
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

	@Enumerated
	public TrangThaiKeHoachVonEnum getTrangThaiKeHoach() {
		return trangThaiKeHoach;
	}
	public void setTrangThaiKeHoach(TrangThaiKeHoachVonEnum trangThaiKeHoach) {
		this.trangThaiKeHoach = trangThaiKeHoach;
	}
	
	@Command
	public void saveKeHoachNhuCauVon(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr, 
			@BindingParam("xacNhan") final boolean xacNhan,
			@BindingParam("wdn") final Window wdn) throws IOException {	
		
		Task task = null;
		if (getCurrentTask() == null) {
			save();
			Map<String, Object> variables = new HashMap<>();
			variables.put("user", core().getNhanVien().getId());
			ProcessInstance processInstance = core()
				.getProcess()
				.getRuntimeService()
				.startProcessInstanceByKey("taoKeHoachNhuCauVon", businessKey(), variables);
			
			task = core()
					.getProcess()
					.getTaskService()
					.createTaskQuery()
	                .processInstanceId(processInstance.getId())
	                //.taskCandidateGroup("dev-managers")
	                .singleResult();
		} else {
			task = getCurrentTask();
		}
		Map<String, Object> variables = new HashMap<>();
		variables.put("xacNhan", xacNhan);
		core().getProcess().getTaskService().complete(task.getId(), variables);


		setTrangThaiKeHoach(xacNhan ? TrangThaiKeHoachVonEnum.CHO_DUYET : TrangThaiKeHoachVonEnum.LUU_TAM);
		saveNotShowNotification();
		
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, attr);		
	}
}
