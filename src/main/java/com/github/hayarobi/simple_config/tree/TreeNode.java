package com.github.hayarobi.simple_config.tree;

import java.util.List;

/**
 * 설정 파일에서 읽어온 설정을 항목에 대한 트리 노드 
 * @author sg13park
 *
 */
public interface TreeNode {
	public String getName();
	public String getFullName();
	public boolean isLeaf();
	public String getValueAsString() throws IllegalCallException;
	public String[] getValueAsStringArray() throws IllegalCallException;
	public List<TreeNode> getChildren() throws IllegalCallException;
	
}
