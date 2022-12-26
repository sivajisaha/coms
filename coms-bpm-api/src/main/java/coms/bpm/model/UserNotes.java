package coms.bpm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserNotes {
	String notes;
	String user;
	Date note_date;
	public UserNotes(String notes,String user,Date notedate)
	{
		setNotes(notes);
		setUser(user);
		setNote_date(notedate);
	}
}
