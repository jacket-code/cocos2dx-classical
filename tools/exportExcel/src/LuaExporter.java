import java.io.File;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class LuaExporter extends BaseExporter {
	@Override
	public void doExport(Workbook book, Sheet sheet, File file) throws IOException {
		File outputDir = file.getParentFile();
		String className = "X" + firstCapital(sheet.getSheetName());
		
		// name and type of id column
		Row fieldRow = sheet.getRow(2);
		Row typeRow = sheet.getRow(3);
		String idName = fieldRow.getCell(0).getStringCellValue();
		String idType = typeRow.getCell(0).getStringCellValue();
		boolean idIsString = idType.equalsIgnoreCase("string");
		
		// json directory prefix
		String jsonDir = getOption("jsonDir");
		
		// lua file
		StringBuilder lfile = new StringBuilder();
		lfile.append("-- Auto generated by exportExcel, don't modify it\n")
			.append("\n")
			.append("-- class\n")
			.append(className + " = class(\"" + className + "\", function() return CCObject:create() end)\n")
			.append("\n")
			.append("-- shared json instance\n")
			.append("local sJSON = nil\n")
			.append("local sLoaded = false\n")
			.append("local sCount = 0\n")
			.append("\n")
			.append("function " + className + ".ensureLoaded()\n")
			.append("\tif not sLoaded then\n")
			.append("\t\tlocal fullPath = CCUtils:getExternalOrFullPath(\"" + jsonDir + ("".equals(jsonDir) ? "" : "/") + className + ".json\")\n")
			.append("\t\tlocal raw = CCResourceLoader:loadString(fullPath)\n")
			.append("\t\tsJSON = json.decode(raw)\n")
			.append("\t\tsLoaded = true\n")
			.append("\t\tsCount = len(sJSON)\n")
			.append("\tend\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ".create(key)\n")
			.append("\t" + className + ".ensureLoaded()\n")
			.append("\tlocal instance = " + className + ".new()\n")
			.append("\tif instance:initWithKey(key) then\n")
			.append("\t\treturn instance\n")
			.append("\tend\n")
			.append("\treturn nil\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ".createWithIndex(index)\n")
			.append("\t" + className + ".ensureLoaded()\n")
			.append("\tif index < 1 or index > " + className + ".count() then\n")
			.append("\t\treturn nil\n")
			.append("\telse\n")
			.append("\t\tlocal instance = " + className + ".new()\n")
			.append("\t\tif instance:initWithIndex(index) then\n")
			.append("\t\t\treturn instance\n")
			.append("\t\tend\n")
			.append("\t\treturn nil\n")
			.append("\tend\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ".createEmpty(_id)\n")
			.append("\tlocal instance = " + className + ".new()\n")
			.append("\tif instance:initWithId(_id) then\n")
			.append("\t\treturn instance\n")
			.append("\tend\n")
			.append("\treturn nil\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ".count()\n")
			.append("\t" + className + ".ensureLoaded()\n")
			.append("\treturn sCount\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ".indexOf(x)\n")
			.append("\tlocal isObj = tolua.isa(x, \"" + className + "\")\n")
			.append("\tif isObj then\n")
			.append("\t\tfor _,item in pairs(sJSON) do\n")
			.append("\t\t\tif " + (idIsString ? "tostring(" : "string.toint(") + "item[\"" + firstCapital(idName) + "\"]) == x:get" + firstCapital(idName) + "() then\n")
			.append("\t\t\t\treturn string.toint(item[\"__index__\"])\n")
			.append("\t\t\tend\n")
			.append("\t\tend\n")
			.append("\telse\n")
			.append("\t\tfor _,item in pairs(sJSON) do\n")
			.append("\t\t\tif " + (idIsString ? "tostring(" : "string.toint(") + "item[\"" + firstCapital(idName) + "\"]) == x then\n")
			.append("\t\t\t\treturn string.toint(item[\"__index__\"])\n")
			.append("\t\t\tend\n")
			.append("\t\tend\n")
			.append("\tend\n")
			.append("\treturn -1\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ":initWithIndex(index)\n")
			.append("\tfor _,item in pairs(sJSON) do\n")
			.append("\t\tif string.toint(item[\"__index__\"]) == index then\n")
			.append("\t\t\treturn self:initWithValue(item)\n")
			.append("\t\tend\n")
			.append("\tend\n")
			.append("\treturn false\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ":initWithKey(key)\n")
			.append("\tlocal item = sJSON[tostring(key)]\n")
			.append("\tif item ~= nil then\n")
			.append("\t\treturn self:initWithValue(item)\n")
			.append("\telse\n")
			.append("\t\treturn false\n")
			.append("\tend\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ":initWithValue(item)\n");
		
		// body of initWithValue
		int len = sheet.getRow(2).getLastCellNum();
		for (int i = 0; i < len; i++) {
			if (sheet.getRow(3).getCell(i) == null || sheet.getRow(3).getCell(i).getStringCellValue().equals(""))
				continue;
			if(sheet.getRow(2).getCell(i) == null || sheet.getRow(3).getCell(i) == null)
				continue;
			String field = sheet.getRow(2).getCell(i).getStringCellValue();
			String dataType = sheet.getRow(3).getCell(i).getStringCellValue();
			if (field == null || field.equals(""))
				continue;
			if (dataType.equalsIgnoreCase("Byte") || dataType.equalsIgnoreCase("int")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.toint(item[\"" + firstCapital(field) + "\"])\n");
			} else if (dataType.equalsIgnoreCase("Float")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.tonumber(item[\"" + firstCapital(field) + "\"])\n");
			} else if (dataType.equalsIgnoreCase("bool")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.tobool(item[\"" + firstCapital(field) + "\"])\n");
			} else if (dataType.equalsIgnoreCase("String")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = tostring(item[\"" + firstCapital(field) + "\"])\n");
			} else if(dataType.equalsIgnoreCase("luafunc")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = tostring(item[\"" + firstCapital(field) + "\"])\n");
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.gsub(self.m_" + firstLowercase(field) + ", \"\\\\n\", \"\\n\")\n");
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.gsub(self.m_" + firstLowercase(field) + ", \"\\\\r\", \"\\r\")\n");
			} else if(dataType.equalsIgnoreCase("StringArray")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = string.split(tostring(item[\"" + firstCapital(field) + "\"]), \",\")\n");
			} else if(dataType.equalsIgnoreCase("IntArray")) {
				lfile.append("\tlocal tmp = string.split(tostring(item[\"" + firstCapital(field) + "\"]), \",\")\n")
					.append("\tself.m_" + firstLowercase(field) + " = {}\n")
					.append("\tfor _,x in ipairs(tmp) do\n")
					.append("\t\ttable.insert(self.m_" + firstLowercase(field) + ", string.toint(x))\n")
					.append("\tend\n");
			} else if(dataType.equalsIgnoreCase("FloatArray")) {
				lfile.append("\tlocal tmp = string.split(tostring(item[\"" + firstCapital(field) + "\"]), \",\")\n")
					.append("\tself.m_" + firstLowercase(field) + " = {}\n")
					.append("\tfor _,x in ipairs(tmp) do\n")
					.append("\t\ttable.insert(self.m_" + firstLowercase(field) + ", string.tonumber(x))\n")
					.append("\tend\n");
			} else if(dataType.equalsIgnoreCase("BoolArray")) {
				lfile.append("\tlocal tmp = string.split(tostring(item[\"" + firstCapital(field) + "\"]), \",\")\n")
					.append("\tself.m_" + firstLowercase(field) + " = {}\n")
					.append("\tfor _,x in ipairs(tmp) do\n")
					.append("\t\ttable.insert(self.m_" + firstLowercase(field) + ", string.tobool(x))\n")
					.append("\tend\n");
			}
		}
		
		// close initWithValue and start initWithId
		lfile.append("\treturn true\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ":initWithId(_id)\n")
			.append("\tself.m_" + firstLowercase(idName) + " = _id\n");
		
		// body of initWithId
		for (int i = 0; i < len; i++) {
			if (sheet.getRow(3).getCell(i) == null || sheet.getRow(3).getCell(i).getStringCellValue().equals(""))
				continue;
			if(sheet.getRow(2).getCell(i) == null || sheet.getRow(3).getCell(i) == null)
				continue;
			String field = sheet.getRow(2).getCell(i).getStringCellValue();
			String dataType = sheet.getRow(3).getCell(i).getStringCellValue();
			if (field == null || field.equals("") || idName.equals(field))
				continue;
			if (dataType.equalsIgnoreCase("Byte") || dataType.equalsIgnoreCase("int") || dataType.equalsIgnoreCase("Float")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = 0\n");
			} else if (dataType.equalsIgnoreCase("bool")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = false\n");
			} else if (dataType.equalsIgnoreCase("String") || dataType.equalsIgnoreCase("luafunc")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = \"\"\n");
			} else if(dataType.equalsIgnoreCase("StringArray") || 
					dataType.equalsIgnoreCase("IntArray") || 
					dataType.equalsIgnoreCase("FloatArray") || 
					dataType.equalsIgnoreCase("BoolArray")) {
				lfile.append("\tself.m_" + firstLowercase(field) + " = {}\n");
			}
		}
		
		// close initWithId and start toValue
		lfile.append("\treturn true\n")
			.append("end\n")
			.append("\n")
			.append("function " + className + ":toValue()\n")
			.append("\tlocal v = {}\n");
		
		// body of toValue
		for (int i = 0; i < len; i++) {
			if (sheet.getRow(3).getCell(i) == null || sheet.getRow(3).getCell(i).getStringCellValue().equals(""))
				continue;
			if(sheet.getRow(2).getCell(i) == null || sheet.getRow(3).getCell(i) == null)
				continue;
			String field = sheet.getRow(2).getCell(i).getStringCellValue();
			String dataType = sheet.getRow(3).getCell(i).getStringCellValue();
			if (field == null || field.equals(""))
				continue;
			if(dataType.equalsIgnoreCase("luafunc")) {
				lfile.append("\tlocal tmp = string.gsub(self.m_" + firstLowercase(field) + ", \"\\r\", \"\\\\r\")\n");
				lfile.append("\ttmp = string.gsub(tmp, \"\\n\", \"\\\\n\")\n");
				lfile.append("\tv[\"" + firstCapital(field) + "\"] = tmp\n");	
			} else if(dataType.equalsIgnoreCase("StringArray") ||
					dataType.equalsIgnoreCase("IntArray") ||
					dataType.equalsIgnoreCase("FloatArray") ||
					dataType.equalsIgnoreCase("BoolArray")) {
				lfile.append("\tv[\"" + firstCapital(field) + "\"] = table.concat(self.m_" + firstLowercase(field) + ", \",\")\n");	
			} else {
				lfile.append("\tv[\"" + firstCapital(field) + "\"] = self.m_" + firstLowercase(field) + "\n");	
			}
		}
		
		// close toValue
		lfile.append("\treturn v\n")
			.append("end\n");
		
		// get method for array types
		for (int i = 0; i < len; i++) {
			if (sheet.getRow(3).getCell(i) == null || sheet.getRow(3).getCell(i).getStringCellValue().equals(""))
				continue;
			if(sheet.getRow(2).getCell(i) == null || sheet.getRow(3).getCell(i) == null)
				continue;
			String field = sheet.getRow(2).getCell(i).getStringCellValue();
			String dataType = sheet.getRow(3).getCell(i).getStringCellValue();
			if (field == null || field.equals(""))
				continue;

			// getter and setter
			// for array, we have more method, such as get count, index of
			if(dataType.equalsIgnoreCase("StringArray") ||
					dataType.equalsIgnoreCase("IntArray") || 
					dataType.equalsIgnoreCase("FloatArray") ||
					dataType.equalsIgnoreCase("BoolArray")) {
				lfile.append("\nfunction " + className + ":get" + firstCapital(field) + "()\n")
					.append("\treturn self.m_" + firstLowercase(field) + "\n")
					.append("end\n")
					.append("\nfunction " + className + ":set" + firstCapital(field) + "(v)\n")
					.append("\tif type(v) ~= \"table\" then\n")
					.append("\t\treturn\n")
					.append("\tend\n")
					.append("\tself.m_" + firstLowercase(field) + " = v\n")
					.append("end\n")
					.append("\nfunction " + className + ":indexOf" + firstCapital(field) + "(v)\n")
					.append("\tfor i,_v in ipairs(self.m_" + firstLowercase(field) + ") do\n")
					.append("\t\tif _v == v then\n")
					.append("\t\t\treturn i\n")
					.append("\t\tend\n")
					.append("\tend\n")
					.append("\treturn -1\n")
					.append("end\n")
					.append("\nfunction " + className + ":get" + firstCapital(field) + "At(index)\n")
					.append("\tif index < 1 or index > #self.m_" + firstLowercase(field) + " then\n")
					.append("\t\treturn false\n")
					.append("\tend\n")
					.append("\treturn self.m_" + firstLowercase(field) + "[index]\n")
					.append("end\n")
					.append("\nfunction " + className + ":get" + firstCapital(field) + "Count()\n")
					.append("\treturn #self.m_" + firstLowercase(field) + "\n")
					.append("end\n");
			} else if(dataType.equalsIgnoreCase("string") || dataType.equalsIgnoreCase("luafunc")) {
				lfile.append("\nfunction " + className + ":get" + firstCapital(field) + "()\n")
					.append("\treturn self.m_" + firstLowercase(field) + "\n")
					.append("end\n")
					.append("\nfunction " + className + ":set" + firstCapital(field) + "(v)\n")
					.append("\tself.m_" + firstLowercase(field) + " = tostring(v)\n")
					.append("end\n");
			} else if(dataType.equalsIgnoreCase("bool")) {
				lfile.append("\nfunction " + className + ":is" + firstCapital(field) + "()\n")
					.append("\treturn self.m_" + firstLowercase(field) + "\n")
					.append("end\n")
					.append("\nfunction " + className + ":set" + firstCapital(field) + "(v)\n")
					.append("\tself.m_" + firstLowercase(field) + " = string.tobool(v)\n")
					.append("end\n");
			} else if(dataType.equalsIgnoreCase("float")) {
				lfile.append("\nfunction " + className + ":get" + firstCapital(field) + "()\n")
					.append("\treturn self.m_" + firstLowercase(field) + "\n")
					.append("end\n")
					.append("\nfunction " + className + ":set" + firstCapital(field) + "(v)\n")
					.append("\tself.m_" + firstLowercase(field) + " = string.tonumber(v)\n")
					.append("end\n");
			} else {
				lfile.append("\nfunction " + className + ":get" + firstCapital(field) + "()\n")
					.append("\treturn self.m_" + firstLowercase(field) + "\n")
					.append("end\n")
					.append("\nfunction " + className + ":set" + firstCapital(field) + "(v)\n")
					.append("\tself.m_" + firstLowercase(field) + " = string.toint(v)\n")
					.append("end\n");
			}
		}
		
		try {
			File dstFile = new File(outputDir, className + ".lua");
			writeFile(dstFile.getAbsolutePath(), lfile.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
