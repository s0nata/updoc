/**
 * Returns the enumerated value tagged with the given integer value for the
 * given enum class. If no enum value in the given class is initialized
 * with the given integer tag value, an exception will be thrown.
 *
 * @param <E> the enum class type
 */
public static <E extends Enum> E enumFromInt(Class<E> enumClass, int value) {
        EnumAdapter<E> adapter = WIRE.enumAdapter(enumClass);
        return adapter.fromInt(value);
        }