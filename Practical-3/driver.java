public class driver {
    public static void main(String[] args) {

        // 5 points with 2 repeats
        point[] points = {
            new point(1, 2),
            new point(3, 4),
            new point(1, 2),
            new point(5, 6),
            new point(3, 4)
        };
        int distinct = 0;
        // Check each point against points that appeared earlier
        for (int i = 0; i < points.length; i++) {
            boolean alreadyAppeared = false;
            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) {
                    alreadyAppeared = true;
                    break;
                }
            }
            if (!alreadyAppeared) {
                distinct++;
            }
        }
        System.out.println("Distinct: " + distinct);
    }
}
