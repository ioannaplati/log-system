/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thriftGenerated;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2020-09-19")
public enum Level implements org.apache.thrift.TEnum {
  INFO(0),
  WARN(1),
  ERROR(2);

  private final int value;

  private Level(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static Level findByValue(int value) { 
    switch (value) {
      case 0:
        return INFO;
      case 1:
        return WARN;
      case 2:
        return ERROR;
      default:
        return null;
    }
  }
}
