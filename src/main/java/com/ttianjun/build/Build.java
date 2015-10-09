package com.ttianjun.build;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;
import com.ttianjun.module.TableInfo;

public class Build {
	private static final String root = "C:\\home\\out";
	static String beanName ="Custstore";//dev_stat_rpt
	static String poName ="custstore";//dev_stat_rpt
	static String tableName="custstore";
	static String beanTitle="商户网点";
	public static void main(String[] args) throws IOException {
		
		PropKit.use("jdbc.txt");
		
		DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
        dp.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType("mysql");
        dp.addFilter(wall);
        dp.start();
        
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(true);
        arp.start();
        List<Record> list = TableInfo.dao.getTableInfo(tableName);
		
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("beanName", beanName);
		param.put("beanTitle", beanTitle);
		param.put("poName", poName);
		param.put("list", list);
		build(param);
		
	}
	public static void build(Map<String,Object> map) throws IOException{
		buildModel(map);
		buildCtrl(map);
		buildIndex(map);
		buildView(map);
		buildEdit(map);
	}
	public static String buildTemp(Map<String,Object> map,String fileName) throws IOException {
		
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
		Template t = gt.getTemplate("/template/"+fileName);
		t.binding(map);
		return t.render();
	}
	
	public static String buildModel(Map<String,Object> map) throws IOException {
		String data = buildTemp(map,"template_model.txt");
		FileUtils.writeStringToFile(new File(root,beanName+".java"), data);
		return data;
	}
	public static String buildCtrl(Map<String,Object> map) throws IOException {
		String data = buildTemp(map,"template_ctrl.txt");
		FileUtils.writeStringToFile(new File(root,beanName+"Controller.java"), data);
		return data;
	}
	public static String buildIndex(Map<String,Object> map) throws IOException {
		String data = buildTemp(map,"template_html_index.txt");
		FileUtils.writeStringToFile(new File(root+"/"+poName,"index.html"), data);
		return data;
	}
	public static String buildView(Map<String,Object> map) throws IOException {
		String data = buildTemp(map,"template_html_view.txt");
		FileUtils.writeStringToFile(new File(root+"/"+poName,"view.html"), data);
		return data;
	}
	public static String buildEdit(Map<String,Object> map) throws IOException {
		String data = buildTemp(map,"template_html_edit.txt");
		FileUtils.writeStringToFile(new File(root+"/"+poName,"edit.html"), data);
		return data;
	}
}
