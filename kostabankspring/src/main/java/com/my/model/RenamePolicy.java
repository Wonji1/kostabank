package com.my.model;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import java.io.File;

public class RenamePolicy implements FileRenamePolicy{
	private String board_id;
	public RenamePolicy(String board_id) {
		this.board_id = board_id;
	}
	@Override
	public File rename(File file) {
		String parent = file.getParent();
		String oldName = file.getName();
		File dir = new File(parent);
		File []files = dir.listFiles();
		for(File f: files) {
			String fileName = f.getName();
			String fileNamePre = fileName.substring(0, fileName.indexOf('_'));
			if(board_id.equals(fileNamePre)){
				f.delete();
			}
		}
		int extIdx = oldName.lastIndexOf(".");
		String name = oldName.substring(0, extIdx);
		name = board_id + "_" + name;
		String newName = name + oldName.substring(extIdx);
		File f = new File(parent,  newName);
		return f;
	}

}