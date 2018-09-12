package vn.toancauxanh.cms.service;

import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Messagebox;

import vn.toancauxanh.model.Setting;
import vn.toancauxanh.service.BasicService;

public class SettingService extends BasicService<Setting> {
	Setting setting = super.getSetting();

	private String filepath = "";

	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	@Command
	public void saveSetting() {
		Setting setting1 = getSetting();
		setting1.save();
	}

	@Command
	public void uploadFile(@BindingParam("media") final Media media) {
		if (media.getName().toLowerCase().endsWith(".sql")) {
			String filename = media.getName();
			setFilepath("D:/stnmt/backup/" + filename);
			BindUtils.postNotifyChange(null, null, this, "filepath");
		} else {
			Messagebox.show("Chá»�n táº­p tin theo Ä‘Ãºng Ä‘á»‹nh dáº¡ng (*.sql)");
		}
	}

	@Override
	public Setting getSetting() {
		return setting;
	}

	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				if (getSetting().getWidthMedium() == 0) {
					addInvalidMessage(ctx, "errLabelMedium", "Chiều rộng phải lớn hơn 0.");
				}

				if (getSetting().getWidthMedium() == 0) {
					addInvalidMessage(ctx, "errLabelSmal", "Chiều rộng phải lớn hơn 0.");
				}
			}
		};
	}
}
