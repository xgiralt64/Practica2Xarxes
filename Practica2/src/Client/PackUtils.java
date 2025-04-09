public class PackUtils {

	public static void packByte (byte b,
	                             byte[] buffer, int offset) {
		buffer[offset] = b;
	}

	public static byte unpackByte (byte[] buffer,
	                               int offset) {
		return buffer[offset];
	}

	public static void packShort (short s,
	                              byte[] buffer, int offset) {
		buffer[offset    ] = (byte) (s >> 8);
		buffer[offset + 1] = (byte)  s      ;
	}

	public static short unpackShort (byte[] buffer,
	                                 int offset) {
		return (short)(
		       ((buffer[offset    ]       ) << 8) |
		       ((buffer[offset + 1] & 0xFF)     ));
	}

	public static void packInt (int i,
	                            byte[] buffer, int offset) {
		buffer[offset    ] = (byte) (i >> 24);
		buffer[offset + 1] = (byte) (i >> 16);
		buffer[offset + 2] = (byte) (i >>  8);
		buffer[offset + 3] = (byte)  i       ;
	}

	public static int unpackInt (byte[] buffer,
	                             int offset) {
		return ((buffer[offset    ]       ) << 24) |
		       ((buffer[offset + 1] & 0xFF) << 16) |
		       ((buffer[offset + 2] & 0xFF) <<  8) |
		       ((buffer[offset + 3] & 0xFF)      ) ;
	}

	public static void packLong (long l,
	                             byte[] buffer, int offset)  {
		buffer[offset    ] = (byte) (l >> 56);
		buffer[offset + 1] = (byte) (l >> 48);
		buffer[offset + 2] = (byte) (l >> 40);
		buffer[offset + 3] = (byte) (l >> 32);
		buffer[offset + 4] = (byte) (l >> 24);
		buffer[offset + 5] = (byte) (l >> 16);
		buffer[offset + 6] = (byte) (l >>  8);
		buffer[offset + 7] = (byte)  l       ;
	}

	public static long unpackLong (byte[] buffer,
	                               int offset) {
		return ((long)(buffer[offset    ]       ) << 56) |
		       ((long)(buffer[offset + 1] & 0xFF) << 48) |
		       ((long)(buffer[offset + 2] & 0xFF) << 40) |
		       ((long)(buffer[offset + 3] & 0xFF) << 32) |
		       ((long)(buffer[offset + 4] & 0xFF) << 24) |
		       ((long)(buffer[offset + 5] & 0xFF) << 16) |
		       ((long)(buffer[offset + 6] & 0xFF) <<  8) |
		       ((long)(buffer[offset + 7] & 0xFF)      ) ;
	}

	public static void packFloat (float f,
	                              byte[] buffer, int offset) {
		int bits = Float.floatToRawIntBits (f);
		packInt (bits, buffer, offset);
	}

	public static float unpackFloat (byte[] buffer,
	                                 int offset) {
		int bits = unpackInt (buffer, offset);
		return Float.intBitsToFloat (bits);
	}

	public static void packDouble (double d,
	                               byte[] buffer, int offset) {
		long bits = Double.doubleToRawLongBits (d);
		packLong (bits, buffer, offset);
	}

	public static double unpackDouble (byte[] buffer,
	                                   int offset) {
		long bits = unpackLong (buffer, offset);
		return Double.longBitsToDouble (bits);
	}

	public static void packBoolean (boolean b,
	                                byte[] buffer, int offset) {
		buffer[offset] = b? (byte)1 : (byte)0;
	}

	public static boolean unpackBoolean (byte[] buffer,
	                                     int offset) {
		return buffer[offset] != 0;
	}

	public static void packChar (char c,
	                             byte[] buffer, int offset) {
		buffer[offset    ] = (byte) (c >> 8);
		buffer[offset + 1] = (byte)  c;
	}

	public static char unpackChar (byte[] buffer,
	                               int offset) {
		return (char) ((buffer[offset    ] << 8) |
		               (buffer[offset + 1] & 0xFF));
	}

	public static void packString (String s, int maxLength,
	                               byte[] buffer, int offset) {
		for (int i = 0; i < maxLength; i++) {
			if (i < s.length()) {
				packChar (s.charAt (i), buffer, offset + 2 * i);
			} else {
				// Pad with zeros
				packChar ('\0', buffer, offset + 2 * i);
			}
		}
	}

	public static String unpackString (int maxLength,
	                                   byte[] buffer, int offset) {
		String s = "";
		for (int i = 0; i < maxLength; i++) {
			char c = unpackChar (buffer, offset + 2 * i);
			if (c != '\0') {
				s += c;
			} else {
				break;
			}
		}
		return s;
	}

}
