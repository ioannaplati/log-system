/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thriftGenerated;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2020-09-19")
public enum Temperature implements org.apache.thrift.TEnum {
  ROOM_TEMP(0),
  A_BIT_COLD(1),
  A_BIT_WARM(2),
  TOO_COLD(3),
  TOO_WARM(4);

  private final int value;

  private Temperature(int value) {
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
  public static Temperature findByValue(int value) { 
    switch (value) {
      case 0:
        return ROOM_TEMP;
      case 1:
        return A_BIT_COLD;
      case 2:
        return A_BIT_WARM;
      case 3:
        return TOO_COLD;
      case 4:
        return TOO_WARM;
      default:
        return null;
    }
  }
}
