/**
 * 
 */
package com.github.hayarobi.simple_config.load;

import com.github.hayarobi.simple_config.tree.TreeNode;

/**
 * 설정파일 원본에서 필드 타입에 맞는 값 객체를 추출한다. 
 * @author sg13park
 *
 */
public interface PropValueExtractor<T> {
	public T extractValue(TreeNode node);	
}
