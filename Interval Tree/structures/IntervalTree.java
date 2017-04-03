package structures;

import java.util.ArrayList;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		sortIntervals(intervalsLeft, 'l');
		sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = 
							getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);

	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		
		if(lr == 'l'){
			for(int i = 0;i < intervals.size();i++){
				for(int j = i+1; j < intervals.size(); j++){
					if (intervals.get(i).leftEndPoint > intervals.get(j).leftEndPoint){					
						Interval temp = intervals.get(i);
						intervals.set(i, intervals.get(j));
						intervals.set(j, temp);
					}
				}
			}
		}
		else if(lr == 'r'){
			for(int i = 0;i < intervals.size();i++){
				for(int j = i+1; j < intervals.size(); j++){
					if (intervals.get(i).rightEndPoint > intervals.get(j).rightEndPoint){
						Interval temp = intervals.get(i);
						intervals.set(i, intervals.get(j));
						intervals.set(j, temp);
					}
				}
			}
		}
		
		return;
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {

		ArrayList<Integer> sortedEndPoints = new ArrayList<Integer>();
		
		for(int i = 0;i < leftSortedIntervals.size();i++){
			sortedEndPoints.add(leftSortedIntervals.get(i).leftEndPoint);
		}
		
		for(int i = 0;i < rightSortedIntervals.size();i++){
			sortedEndPoints.add(rightSortedIntervals.get(i).rightEndPoint);
		}
		
		for(int i = 0;i < sortedEndPoints.size();i++){
			for(int j = i+1; j < sortedEndPoints.size(); j++){
				if (sortedEndPoints.get(i) > sortedEndPoints.get(j)){					
					int temp = sortedEndPoints.get(i);
					sortedEndPoints.set(i, sortedEndPoints.get(j));
					sortedEndPoints.set(j, temp);
				}
			}
		}
		
		for(int i = 0;i < sortedEndPoints.size();i++){
			for(int j = i + 1;j < sortedEndPoints.size();j++){
				if(sortedEndPoints.get(i) == sortedEndPoints.get(j)){
					sortedEndPoints.remove(j);
					j = j-1;
				}
			}
		}
		
		return sortedEndPoints;
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points
	 * without duplicates.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {

		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		IntervalTreeNode tree;
		
		for(int i = 0; i < endPoints.size(); i++){
			IntervalTreeNode T = new IntervalTreeNode(endPoints.get(i),endPoints.get(i),endPoints.get(i));
			Q.enqueue(T);
		}
		
		while(true){ 
			
			int s = Q.size();
			
			if(s == 1){
				tree = Q.dequeue();
				return tree;
			}
			
			int temps = s;
			while(temps > 1){
				IntervalTreeNode T1 = Q.dequeue();
				IntervalTreeNode T2 = Q.dequeue();
				float v1 = T1.maxSplitValue;
				float v2 = T2.minSplitValue;
				
				IntervalTreeNode T1temp = T1;
				while(T1temp != null){
					v1 = T1temp.maxSplitValue;
					T1temp = T1temp.rightChild;
				}
				
				IntervalTreeNode T2temp = T2;
				while(T2temp != null){
					v2 = T2temp.minSplitValue;
					T2temp = T2temp.leftChild;
				}
				
				float x = (v1+v2)/2;
				
				IntervalTreeNode N = new IntervalTreeNode(x,T1.minSplitValue,T2.maxSplitValue);
				IntervalTreeNode T = N;
				T.leftChild = T1;
				T.rightChild = T2;
				Q.enqueue(N);
				temps = temps - 2;
			}
			
			if(temps == 1){
				Q.enqueue(Q.dequeue());
			}		
		}
		
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		IntervalTreeNode temp = root;
		
		for(int i = 0; i < leftSortedIntervals.size(); i++){		
			addNode(temp, leftSortedIntervals.get(i), true);
		}
		
		for(int i = 0; i < rightSortedIntervals.size(); i++){		
			addNode(temp, rightSortedIntervals.get(i), false);
		}
		
	}
	
	/**
	 * private helper method for mapIntervalsToTree. 
	 * 
	 * @param tree Intervals tree to be mapped
	 * @param q Intervals to be mapped
	 * @param left Boolean value determining leftIntervals or rightIntervals to be based on
	 */
	private void addNode(IntervalTreeNode tree, Interval q, boolean isLeft){
		
		if(q.contains(tree.splitValue)){
			
			if(isLeft){
				
				if (tree.leftIntervals == null){
					tree.leftIntervals = new ArrayList<Interval>();
				}
				
				tree.leftIntervals.add(q);
			}
			else{
				
				if (tree.rightIntervals == null){
					tree.rightIntervals = new ArrayList<Interval>();
				}
				tree.rightIntervals.add(q);
			}
			return;
		}
	
		if(tree.splitValue < q.leftEndPoint){
			addNode(tree.rightChild, q, isLeft);
		}
		
		else{
			addNode(tree.leftChild, q, isLeft);
		}

	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		
		return intersections(root, q);
	}
	
	/**
	 * private helper method for findIntersectingIntervals. 
	 * 
	 * @param tree Intervals tree to be searched
	 * @param iq The interval to be queried
	 */
	private ArrayList<Interval> intersections(IntervalTreeNode tree, Interval iq){
		
		ArrayList<Interval> resultList = new ArrayList<Interval>();
		IntervalTreeNode curr = tree;
		
		if(curr == null){
			return resultList;
		}
		
		float splitVal = curr.splitValue;
		ArrayList<Interval> lList = tree.leftIntervals;
		ArrayList<Interval> rList = tree.rightIntervals;
		
		if(lList == null){
			lList = new ArrayList<Interval>();
		}
		if(rList == null){
			rList = new ArrayList<Interval>();
		}
		
		IntervalTreeNode lSub = tree.leftChild;
		IntervalTreeNode rSub = tree.rightChild;

		if(iq.contains(splitVal)){	
			resultList.addAll(lList);
			resultList.addAll(intersections(rSub, iq));
			resultList.addAll(intersections(lSub, iq));	
		}

		else if(splitVal < iq.leftEndPoint){	
			int i = rList.size()-1;
			while (i >= 0 && (rList.get(i).intersects(iq))){
				resultList.add(rList.get(i));
				i--;
			}
			resultList.addAll(intersections(rSub, iq));
		}

		else if(splitVal > iq.rightEndPoint){
			int i = 0;
			while ((i < lList.size()) && (lList.get(i).intersects(iq))){
				resultList.add(lList.get(i));						
				i++;
			}
			resultList.addAll(intersections(lSub, iq));
		}
		
		return resultList;
	}
	
	
}
