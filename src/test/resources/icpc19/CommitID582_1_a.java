/**
 * Returns the integer value tagged associated with the given enum instance.
 * If the enum instance is not initialized with an integer tag value, an exception
 * will be thrown.
 *
 * @param <E> the enum class type
 */
@SuppressWarnings("unchecked")
public static <E extends Enum> int intFromEnum(E value) {
        EnumAdapter<E> adapter = WIRE.enumAdapter((Class<E>) value.getClass());
        return adapter.toInt(value);
        }