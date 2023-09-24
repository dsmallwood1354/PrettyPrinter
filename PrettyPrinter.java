import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class used to pretty print query results.
 */
public class PrettyPrinter {
    // Elements that make up the border of the pretty printed tables
    final private String H_BORDER = "-";
    final private String V_BORDER = "|";
    final private String INTERSECTION_BORDER = "+";

    /**
     * Method used to pretty print the result set of a given query.
     * @param rs ResultSet          The result set of a given query.
     */
    public void printTable(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            ArrayList<String[]> dbTableValues = new ArrayList<>();

            dbTableValues.add(new String[numCols]);
            for (int i = 0; i < numCols; i++) {
                dbTableValues.get(0)[i] = rsmd.getColumnName(i + 1);
            }
            while (rs.next()) {
                String[] rowData = new String[numCols];
                // had to be hardcoded in to ensure the DATE value remained in YYYY format
                for (int i = 0; i < numCols; i++) {
                    rowData[i] = rs.getString(i + 1);
                }
                dbTableValues.add(rowData);
            }

            // determines the max widths of the columns of the printed table
            int[] colWidths = getColWidths(dbTableValues);
            String border = horizontalBorder(colWidths);

            // pretty prints the table
            System.out.println("\n" + border);
            for (int i = 0; i < numCols; i++) {
                System.out.print(V_BORDER + String.format(" %-" + (colWidths[i] - 1) + "s",
                        dbTableValues.get(0)[i]));
            }
            System.out.println(V_BORDER + "\n" + border);
            for (int i = 1; i < dbTableValues.size(); i++) {
                for (int j = 0; j < numCols; j++) {
                    System.out.print(V_BORDER + String.format(" %-" + (colWidths[j] - 1) + "s",
                            dbTableValues.get(i)[j]));
                }
                System.out.println(V_BORDER);
            }
            System.out.println(border);
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    /**
     * Method used to determine the maximum widths of the columns of a given table.
     * @param dbValues ArrayList<String[]>      The tables corresponding values.
     * @return int[]                            An int array containing the max column widths.
     */
    private int[] getColWidths(ArrayList<String[]> dbValues) {
        int numCol = dbValues.get(0).length;
        int[] maxColWidths = new int[numCol];

        for (String[] dbValue : dbValues) {
            for (int j = 0; j < numCol; j++) {
                if (dbValue[j].length() > maxColWidths[j]) {
                    maxColWidths[j] = dbValue[j].length();
                }
            }
        }
        for (int i = 0; i < numCol; i++) {
            maxColWidths[i] += 3;
        }

        return maxColWidths;
    }

    /**
     * Method for generating a horizontal border.
     * @param colWidths int[]       Int array consisting of the max column widths.
     * @return String               A String border that is used in pretty printing a table to the CLI.
     */
    private String horizontalBorder(int[] colWidths) {
        StringBuilder border = new StringBuilder();
        for (int colWidth : colWidths) {
            border.append(INTERSECTION_BORDER);
            border.append(H_BORDER.repeat(Math.max(0, colWidth)));
        }
        border.append(INTERSECTION_BORDER);

        return border.toString();
    }
}
