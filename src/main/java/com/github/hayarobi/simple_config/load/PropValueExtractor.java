/**
 * 
 */
package com.github.hayarobi.simple_config.load;

import com.github.hayarobi.simple_config.tree.TreeNode;

/**
 * @author sg13park
 *
 */
public interface PropValueExtractor<T> {
	public T extractValue(TreeNode node);	
}
