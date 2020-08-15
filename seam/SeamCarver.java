/* *****************************************************************************
 *  Name:Gary Gu
 *  Date:7/27/20
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private boolean transposed = false;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
/*        this.picture = new Picture(picture.width(), picture.height());
        for (int row = 0; row < picture.height(); ++row) {
            for (int col = 0; col < picture.width(); ++col) {
                this.picture.setRGB(col, row, picture.getRGB(col, row));
            }
        }*/
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkXY(x, y);
        if (x == 0 || y == 0) return 1000;
        if (x == width() - 1 || y == height() - 1) return 1000;
        int x1rgb = picture.getRGB(x + 1, y);
        int x2rgb = picture.getRGB(x - 1, y);
        int y1rgb = picture.getRGB(x, y + 1);
        int y2rgb = picture.getRGB(x, y - 1);
        double gradientX = gradient(x1rgb, x2rgb);
        double gradientY = gradient(y1rgb, y2rgb);
        return Math.sqrt(gradientX + gradientY);
    }

    private void checkXY(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException();
    }

    private double gradient(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = rgb1 & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = rgb2 & 0xFF;
        double result = Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
        return result;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        Picture newPic = new Picture(height(), width());
        for (int row = 0; row < height(); ++row) {
            for (int col = 0; col < width(); ++col) {
                newPic.setRGB(row, col, picture.getRGB(col, row));
            }
        }
        picture = newPic;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[height()][];
        double[][] distTo = new double[height()][];
        int[][] edgeTo = new int[height()][];

        for (int i = 0; i < height(); ++i) {
            energy[i] = new double[width()];
            distTo[i] = new double[width()];
            edgeTo[i] = new int[width()];
        }
        for (int row = 0; row < height(); ++row) {
            for (int col = 0; col < width(); ++col) {
                energy[row][col] = energy(col, row);
                distTo[row][col] = row == 0 ? 0 : Double.POSITIVE_INFINITY;
                edgeTo[row][col] = -1;
            }
        }
        for (int row = 0; row < height() - 1; ++row) {
            for (int col = 0; col < width(); ++col) {
                relax(energy, distTo, edgeTo, row + 1, col, col - 1);
                relax(energy, distTo, edgeTo, row + 1, col, col);
                relax(energy, distTo, edgeTo, row + 1, col, col + 1);
            }
        }
        double minEnergy = Double.POSITIVE_INFINITY;
        int minIdx = 0;
        for (int col = 0; col < width(); ++col) {
            if (minEnergy > distTo[height() - 1][col]) {
                minEnergy = distTo[height() - 1][col];
                minIdx = col;
            }
        }
        int[] seam = new int[height()];
        seam[height() - 1] = minIdx;
        for (int row = height() - 2; row >= 0; --row) {
            seam[row] = edgeTo[row + 1][seam[row + 1]];
        }
        return seam;
    }

    private void relax(double[][] energy, double[][] distTo, int[][] edgeTo, int row, int colFrom,
                       int colTo) {
        if (colTo < 0 || colTo >= width()) return;
        if (distTo[row][colTo] > distTo[row - 1][colFrom] + energy[row][colTo]) {
            distTo[row][colTo] = distTo[row - 1][colFrom] + energy[row][colTo];
            edgeTo[row][colTo] = colFrom;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        for (int col = 0; col < width(); ++col) {
            if (seam[col] < 0 || seam[col] >= height()) throw new IllegalArgumentException();
            if (col > 0)
                if (Math.abs(seam[col] - seam[col - 1]) > 1) throw new IllegalArgumentException();
        }
        Picture trimmed = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); ++col) {
            for (int row = 0; row < height() - 1; ++row) {

                int targetRow = row < seam[col] ? row : row + 1;
                trimmed.setRGB(col, row, picture.getRGB(col, targetRow));
            }
        }
        picture = trimmed;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        for (int row = 0; row < height(); ++row) {
            if (seam[row] < 0 || seam[row] >= width()) throw new IllegalArgumentException();
            if (row > 0)
                if (Math.abs(seam[row] - seam[row - 1]) > 1) throw new IllegalArgumentException();
        }
        Picture trimmed = new Picture(width() - 1, height());
        for (int row = 0; row < height(); ++row) {
            for (int col = 0; col < width() - 1; ++col) {
                int targetCol = col < seam[row] ? col : col + 1;
                trimmed.setRGB(col, row, picture.getRGB(targetCol, row));
            }
        }
        picture = trimmed;
    }

    public static void main(String[] args) {
        // leave this block empty
    }
}
