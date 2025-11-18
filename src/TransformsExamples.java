import transforms.*;

public class TransformsExamples {

    public static void main(String[] args) {
        vectorOperations();
        matrixOperations();
        // TODO transformationTask()
    }

    private static void vectorOperations() {
        Point2D a = new Point2D(-3, 7);
        Point2D b = new Point2D(4,5);

        Vec2D ab = new Vec2D(
                b.getX() - a.getX(),
                b.getY() - a.getY());

        System.out.println(ab);

        System.out.println("=== VECTOR OPERATIONS ===\n");

        // Creating vectors
        Vec2D v1 = new Vec2D(3.0, 4.0);
        Vec2D v2 = new Vec2D(1.0, 2.0);

        System.out.println("v1: " + v1);
        System.out.println("v2: " + v2);

        // Vector addition
        Vec2D sum = v1.add(v2);
        System.out.println("v1 + v2: " + sum);

        // Vector subtraction
        Vec2D diff = v1.sub(v2);
        System.out.println("v1 - v2: " + diff);

        // Scalar multiplication
        Vec2D scaled = v1.mul(2.0);
        System.out.println("v1 * 2: " + scaled);

        // Dot product = Skalárny súčin
        double dotProduct = v1.dot(v2);
        System.out.println("v1 · v2: " + dotProduct);

        // Length
        double length = v1.length();
        System.out.println("Length of v1: " + length);

        // Normalization
        Vec2D normalized = v1.normalized().orElse(new Vec2D());
        System.out.println("Normalized v1: " + normalized);

        // 3D Cross product = Vektorový súčin
        Vec3D aCross = new Vec3D(1, 1.5, 2);
        Vec3D bCross = new Vec3D(2, 1, 0);
        Vec3D cCross = aCross.cross(bCross);
        System.out.println(aCross + " × "+ bCross + " : " + cCross + "\n");
    }

    private static void matrixOperations() {
        System.out.println("=== MATRIX OPERATIONS ===\n");

        // Identity matrix
        Mat3 identity = new Mat3Identity();
        System.out.println("Identity matrix:");
        System.out.println(identity);

        // Creating custom matrices
        Vec3D row1 = new Vec3D(1, 2,3);
        Vec3D row2 = new Vec3D(4, 5, 6);
        Vec3D row3 = new Vec3D(7, 8, 9);
        Mat3 m1 = new Mat3(row1, row2, row3);

        System.out.println("My custom matrix m1:");
        System.out.println(m1);

        // Matrix multiplication
        Mat3 product = m1.mul(identity);
        System.out.println("m1 * identity:");
        System.out.println(product);

        // Scalar multiplication
        Mat3 changed = m1.mul(2.0);
        System.out.println("m1 * 2:");
        System.out.println(changed);

        Mat3 rotX = new Mat3RotX(Math.PI / 4);
        Mat3 rotZ = new Mat3RotZ(Math.PI / 2);

        changed = changed.mul(rotZ).mul(rotX);
        System.out.println("Rotated matrix:");
        System.out.println(changed);

        // Transpose
        Mat3 transposed = m1.transpose();
        System.out.println("Transpose of m1:");
        System.out.println(transposed);
    }

    private static void transformationTask() {
        System.out.println("=== TRANSFORMATION TASK ===\n");

        Point3D point = new Point3D(-2.0, 3.0, 8.0);

        // Translation
        Mat3 translation = new Mat3Transl2D(5.0, 10.0);
        // TODO try to move m1 matrix or point

        // Scaling
        Mat3 scaling = new Mat3Scale2D(2.0, 2.0);
        // TODO try to scale m1 matrix

        // Rotation - Must be done in radians
        // https://en.wikipedia.org/wiki/Radian
        double angle1 = Math.PI / 4; // (45 degrees = π/4 radian)
        double angle2 = Math.PI / 3; // (60 degrees = π/3 radian)
        double angle3 = Math.PI / 2; // (90 degrees = π/2 radian)
        double angle4 = Math.PI;     // (180 degrees = π radian)
        // TODO Try to rotate vec3d, matrix around X axis, Y axis, Z axis ...

        // TODO Try to combined transformation: Scale -> Rotate -> Translate

    }


}
