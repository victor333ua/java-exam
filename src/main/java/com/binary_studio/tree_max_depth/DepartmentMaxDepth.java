package com.binary_studio.tree_max_depth;

import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public final class DepartmentMaxDepth {

	private DepartmentMaxDepth() {
	}

	public static Integer calculateMaxDepth(Department root) {
		int curLength = 1, maxLength = 1;
		Department node;
		Stack<Department> stack = new Stack<>();

		if (root == null)
			return 0;
		if (root.subDepartments.isEmpty())
			return 1;

		stack.push(root);
		node = root;

		for (;;) {

			System.out.println(node.name);

			Optional<Department> optNode = node.subDepartments.stream().filter(Objects::nonNull)
					.filter(depart -> !depart.isVisited).findFirst();
			if (optNode.isPresent()) {
				node = optNode.get();
				// end of branch
				if (!node.subDepartments.stream().anyMatch(Objects::nonNull)) {
					if (curLength + 1 > maxLength) maxLength = curLength + 1;
					node.isVisited = true;
					continue;
				}
				// move to next level
				stack.push(node);
				node.isVisited = true;
				curLength++;
			}
			// end of children's list
			else {
				if (stack.isEmpty())
					return maxLength;
				node = stack.pop();
			}
		}
	}

}
