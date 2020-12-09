package com.hh.hlibrary.utils

import java.io.FilterOutputStream

/**
 * This class consists exclusively of static methods for obtaining
 * encoders and decoders for the Base64 encoding scheme. The
 * implementation of this class supports the following types of Base64
 * as specified in
 * [RFC 4648](http://www.ietf.org/rfc/rfc4648.txt) and
 * [RFC 2045](http://www.ietf.org/rfc/rfc2045.txt).
 *
 *
 *  * <a name="basic">**Basic**</a>
 *
 *  Uses "The Base64 Alphabet" as specified in Table 1 of
 * RFC 4648 and RFC 2045 for encoding and decoding operation.
 * The encoder does not add any line feed (line separator)
 * character. The decoder rejects data that contains characters
 * outside the base64 alphabet.
 *
 *  * <a name="url">**URL and Filename safe**</a>
 *
 *  Uses the "URL and Filename safe Base64 Alphabet" as specified
 * in Table 2 of RFC 4648 for encoding and decoding. The
 * encoder does not add any line feed (line separator) character.
 * The decoder rejects data that contains characters outside the
 * base64 alphabet.
 *
 *  * <a name="mime">**MIME**</a>
 *
 *  Uses the "The Base64 Alphabet" as specified in Table 1 of
 * RFC 2045 for encoding and decoding operation. The encoded output
 * must be represented in lines of no more than 76 characters each
 * and uses a carriage return `'\r'` followed immediately by
 * a linefeed `'\n'` as the line separator. No line separator
 * is added to the end of the encoded output. All line separators
 * or other characters not found in the base64 alphabet table are
 * ignored in decoding operation.
 *
 *
 *
 *  Unless otherwise noted, passing a `null` argument to a
 * method of this class will cause a [ NullPointerException][NullPointerException] to be thrown.
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object Base64 {
    /**
     * Returns a [Encoder] that encodes using the
     * [Basic](#basic) type base64 encoding scheme.
     *
     * @return  A Base64 encoder.
     */
    fun getEncoder(): Encoder? {
        return Encoder.RFC4648
    }

    /**
     * Returns a [Encoder] that encodes using the
     * [URL and Filename safe](#url) type base64
     * encoding scheme.
     *
     * @return  A Base64 encoder.
     */
    fun getUrlEncoder(): Encoder? {
        return Encoder.RFC4648_URLSAFE
    }

    /**
     * Returns a [Encoder] that encodes using the
     * [MIME](#mime) type base64 encoding scheme.
     *
     * @return  A Base64 encoder.
     */
    fun getMimeEncoder(): Encoder? {
        return Encoder.RFC2045
    }

    /**
     * Returns a [Encoder] that encodes using the
     * [MIME](#mime) type base64 encoding scheme
     * with specified line length and line separators.
     *
     * @param   lineLength
     * the length of each output line (rounded down to nearest multiple
     * of 4). If `lineLength <= 0` the output will not be separated
     * in lines
     * @param   lineSeparator
     * the line separator for each output line
     *
     * @return  A Base64 encoder.
     *
     * @throws  IllegalArgumentException if `lineSeparator` includes any
     * character of "The Base64 Alphabet" as specified in Table 1 of
     * RFC 2045.
     */
    fun getMimeEncoder(lineLength: Int, lineSeparator: ByteArray?): Encoder? {
        Objects.requireNonNull(lineSeparator)
        val base64: IntArray? = Decoder.fromBase64
        for (b: Byte in lineSeparator) {
            if (base64.get(b and 0xff) != -1) throw IllegalArgumentException(
                "Illegal base64 line separator character 0x" + Integer.toString(b, 16)
            )
        }
        if (lineLength <= 0) {
            return Encoder.RFC4648
        }
        return Encoder(false, lineSeparator, lineLength shr 2 shl 2, true)
    }

    /**
     * Returns a [Decoder] that decodes using the
     * [Basic](#basic) type base64 encoding scheme.
     *
     * @return  A Base64 decoder.
     */
    fun getDecoder(): Decoder? {
        return Decoder.RFC4648
    }

    /**
     * Returns a [Decoder] that decodes using the
     * [URL and Filename safe](#url) type base64
     * encoding scheme.
     *
     * @return  A Base64 decoder.
     */
    fun getUrlDecoder(): Decoder? {
        return Decoder.RFC4648_URLSAFE
    }

    /**
     * Returns a [Decoder] that decodes using the
     * [MIME](#mime) type base64 decoding scheme.
     *
     * @return  A Base64 decoder.
     */
    fun getMimeDecoder(): Decoder? {
        return Decoder.RFC2045
    }

    /**
     * This class implements an encoder for encoding byte data using
     * the Base64 encoding scheme as specified in RFC 4648 and RFC 2045.
     *
     *
     *  Instances of [Encoder] class are safe for use by
     * multiple concurrent threads.
     *
     *
     *  Unless otherwise noted, passing a `null` argument to
     * a method of this class will cause a
     * [NullPointerException] to
     * be thrown.
     *
     * @see Decoder
     *
     * @since   1.8
     */
    class Encoder private constructor(
        private val isURL: Boolean,
        private val newline: ByteArray?,
        private val linemax: Int,
        private val doPadding: Boolean
    ) {
        private fun outLength(srclen: Int): Int {
            var len: Int = 0
            if (doPadding) {
                len = 4 * ((srclen + 2) / 3)
            } else {
                val n: Int = srclen % 3
                len = 4 * (srclen / 3) + (if (n == 0) 0 else n + 1)
            }
            if (linemax > 0) // line separators
                len += (len - 1) / linemax * newline.size
            return len
        }

        /**
         * Encodes all bytes from the specified byte array into a newly-allocated
         * byte array using the [Base64] encoding scheme. The returned byte
         * array is of the length of the resulting bytes.
         *
         * @param   src
         * the byte array to encode
         * @return  A newly-allocated byte array containing the resulting
         * encoded bytes.
         */
        fun encode(src: ByteArray?): ByteArray? {
            val len: Int = outLength(src.size) // dst array size
            val dst: ByteArray? = ByteArray(len)
            val ret: Int = encode0(src, 0, src.size, dst)
            if (ret != dst.size) return Arrays.copyOf(dst, ret)
            return dst
        }

        /**
         * Encodes all bytes from the specified byte array using the
         * [Base64] encoding scheme, writing the resulting bytes to the
         * given output byte array, starting at offset 0.
         *
         *
         *  It is the responsibility of the invoker of this method to make
         * sure the output byte array `dst` has enough space for encoding
         * all bytes from the input byte array. No bytes will be written to the
         * output byte array if the output byte array is not big enough.
         *
         * @param   src
         * the byte array to encode
         * @param   dst
         * the output byte array
         * @return  The number of bytes written to the output byte array
         *
         * @throws  IllegalArgumentException if `dst` does not have enough
         * space for encoding all input bytes.
         */
        fun encode(src: ByteArray?, dst: ByteArray?): Int {
            val len: Int = outLength(src.size) // dst array size
            if (dst.size < len) throw IllegalArgumentException(
                "Output byte array is too small for encoding all input bytes"
            )
            return encode0(src, 0, src.size, dst)
        }

        /**
         * Encodes the specified byte array into a String using the [Base64]
         * encoding scheme.
         *
         *
         *  This method first encodes all input bytes into a base64 encoded
         * byte array and then constructs a new String by using the encoded byte
         * array and the [ ISO-8859-1][StandardCharsets.ISO_8859_1] charset.
         *
         *
         *  In other words, an invocation of this method has exactly the same
         * effect as invoking
         * `new String(encode(src), StandardCharsets.ISO_8859_1)`.
         *
         * @param   src
         * the byte array to encode
         * @return  A String containing the resulting Base64 encoded characters
         */
        @SuppressWarnings("deprecation")
        fun encodeToString(src: ByteArray?): String? {
            val encoded: ByteArray? = encode(src)
            return String(encoded, 0, 0, encoded.size)
        }

        /**
         * Encodes all remaining bytes from the specified byte buffer into
         * a newly-allocated ByteBuffer using the [Base64] encoding
         * scheme.
         *
         * Upon return, the source buffer's position will be updated to
         * its limit; its limit will not have been changed. The returned
         * output buffer's position will be zero and its limit will be the
         * number of resulting encoded bytes.
         *
         * @param   buffer
         * the source ByteBuffer to encode
         * @return  A newly-allocated byte buffer containing the encoded bytes.
         */
        fun encode(buffer: ByteBuffer?): ByteBuffer? {
            val len: Int = outLength(buffer.remaining())
            var dst: ByteArray? = ByteArray(len)
            var ret: Int = 0
            if (buffer.hasArray()) {
                ret = encode0(
                    buffer.array(),
                    buffer.arrayOffset() + buffer.position(),
                    buffer.arrayOffset() + buffer.limit(),
                    dst
                )
                buffer.position(buffer.limit())
            } else {
                val src: ByteArray? = ByteArray(buffer.remaining())
                buffer.get(src)
                ret = encode0(src, 0, src.size, dst)
            }
            if (ret != dst.size) dst = Arrays.copyOf(dst, ret)
            return ByteBuffer.wrap(dst)
        }

        /**
         * Wraps an output stream for encoding byte data using the [Base64]
         * encoding scheme.
         *
         *
         *  It is recommended to promptly close the returned output stream after
         * use, during which it will flush all possible leftover bytes to the underlying
         * output stream. Closing the returned output stream will close the underlying
         * output stream.
         *
         * @param   os
         * the output stream.
         * @return  the output stream for encoding the byte data into the
         * specified Base64 encoded format
         */
        fun wrap(os: OutputStream?): OutputStream? {
            Objects.requireNonNull(os)
            return EncOutputStream(
                os, if (isURL) toBase64URL else toBase64,
                newline, linemax, doPadding
            )
        }

        /**
         * Returns an encoder instance that encodes equivalently to this one,
         * but without adding any padding character at the end of the encoded
         * byte data.
         *
         *
         *  The encoding scheme of this encoder instance is unaffected by
         * this invocation. The returned encoder instance should be used for
         * non-padding encoding operation.
         *
         * @return an equivalent encoder that encodes without adding any
         * padding character at the end
         */
        fun withoutPadding(): Encoder? {
            if (!doPadding) return this
            return Encoder(isURL, newline, linemax, false)
        }

        private fun encode0(src: ByteArray?, off: Int, end: Int, dst: ByteArray?): Int {
            val base64: CharArray? = if (isURL) toBase64URL else toBase64
            var sp: Int = off
            var slen: Int = (end - off) / 3 * 3
            val sl: Int = off + slen
            if (linemax > 0 && slen > linemax / 4 * 3) slen = linemax / 4 * 3
            var dp: Int = 0
            while (sp < sl) {
                val sl0: Int = Math.min(sp + slen, sl)
                var sp0: Int = sp
                var dp0: Int = dp
                while (sp0 < sl0) {
                    val bits: Int = (((src.get(sp0++) and 0xff) shl 16) or (
                            (src.get(sp0++) and 0xff) shl 8) or
                            (src.get(sp0++) and 0xff))
                    dst.get(dp0++) = base64.get((bits ushr 18) and 0x3f) as Byte
                    dst.get(dp0++) = base64.get((bits ushr 12) and 0x3f) as Byte
                    dst.get(dp0++) = base64.get((bits ushr 6) and 0x3f) as Byte
                    dst.get(dp0++) = base64.get(bits and 0x3f) as Byte
                }
                val dlen: Int = (sl0 - sp) / 3 * 4
                dp += dlen
                sp = sl0
                if (dlen == linemax && sp < end) {
                    for (b: Byte in newline) {
                        dst.get(dp++) = b
                    }
                }
            }
            if (sp < end) {               // 1 or 2 leftover bytes
                val b0: Int = src.get(sp++) and 0xff
                dst.get(dp++) = base64.get(b0 shr 2) as Byte
                if (sp == end) {
                    dst.get(dp++) = base64.get((b0 shl 4) and 0x3f) as Byte
                    if (doPadding) {
                        dst.get(dp++) = '='
                        dst.get(dp++) = '='
                    }
                } else {
                    val b1: Int = src.get(sp++) and 0xff
                    dst.get(dp++) = base64.get((b0 shl 4) and 0x3f or (b1 shr 4)) as Byte
                    dst.get(dp++) = base64.get((b1 shl 2) and 0x3f) as Byte
                    if (doPadding) {
                        dst.get(dp++) = '='
                    }
                }
            }
            return dp
        }

        companion object {
            /**
             * This array is a lookup table that translates 6-bit positive integer
             * index values into their "Base64 Alphabet" equivalents as specified
             * in "Table 1: The Base64 Alphabet" of RFC 2045 (and RFC 4648).
             */
            private val toBase64: CharArray? = charArrayOf(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
            )

            /**
             * It's the lookup table for "URL and Filename safe Base64" as specified
             * in Table 2 of the RFC 4648, with the '+' and '/' changed to '-' and
             * '_'. This table is used when BASE64_URL is specified.
             */
            private val toBase64URL: CharArray? = charArrayOf(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
            )
            private val MIMELINEMAX: Int = 76
            private val CRLF: ByteArray? = byteArrayOf('\r'.toByte(), '\n'.toByte())
            val RFC4648: Encoder? = Encoder(false, null, -1, true)
            val RFC4648_URLSAFE: Encoder? = Encoder(true, null, -1, true)
            val RFC2045: Encoder? = Encoder(false, CRLF, MIMELINEMAX, true)
        }
    }

    /**
     * This class implements a decoder for decoding byte data using the
     * Base64 encoding scheme as specified in RFC 4648 and RFC 2045.
     *
     *
     *  The Base64 padding character `'='` is accepted and
     * interpreted as the end of the encoded byte data, but is not
     * required. So if the final unit of the encoded byte data only has
     * two or three Base64 characters (without the corresponding padding
     * character(s) padded), they are decoded as if followed by padding
     * character(s). If there is a padding character present in the
     * final unit, the correct number of padding character(s) must be
     * present, otherwise `IllegalArgumentException` (
     * `IOException` when reading from a Base64 stream) is thrown
     * during decoding.
     *
     *
     *  Instances of [Decoder] class are safe for use by
     * multiple concurrent threads.
     *
     *
     *  Unless otherwise noted, passing a `null` argument to
     * a method of this class will cause a
     * [NullPointerException] to
     * be thrown.
     *
     * @see Encoder
     *
     * @since   1.8
     */
    class Decoder private constructor(private val isURL: Boolean, private val isMIME: Boolean) {
        companion object {
            /**
             * Lookup table for decoding unicode characters drawn from the
             * "Base64 Alphabet" (as specified in Table 1 of RFC 2045) into
             * their 6-bit positive integer equivalents.  Characters that
             * are not in the Base64 alphabet but fall within the bounds of
             * the array are encoded to -1.
             *
             */
            private val fromBase64: IntArray? = IntArray(256)

            /**
             * Lookup table for decoding "URL and Filename safe Base64 Alphabet"
             * as specified in Table2 of the RFC 4648.
             */
            private val fromBase64URL: IntArray? = IntArray(256)
            val RFC4648: Decoder? = Decoder(false, false)
            val RFC4648_URLSAFE: Decoder? = Decoder(true, false)
            val RFC2045: Decoder? = Decoder(false, true)

            init {
                Arrays.fill(fromBase64, -1)
                for (i in Encoder.toBase64.indices) fromBase64.get(Encoder.toBase64.get(i)) = i
                fromBase64.get('='.toInt()) = -2
            }

            init {
                Arrays.fill(fromBase64URL, -1)
                for (i in Encoder.toBase64URL.indices) fromBase64URL.get(Encoder.toBase64URL.get(i)) =
                    i
                fromBase64URL.get('='.toInt()) = -2
            }
        }

        /**
         * Decodes all bytes from the input byte array using the [Base64]
         * encoding scheme, writing the results into a newly-allocated output
         * byte array. The returned byte array is of the length of the resulting
         * bytes.
         *
         * @param   src
         * the byte array to decode
         *
         * @return  A newly-allocated byte array containing the decoded bytes.
         *
         * @throws  IllegalArgumentException
         * if `src` is not in valid Base64 scheme
         */
        fun decode(src: ByteArray?): ByteArray? {
            var dst: ByteArray? = ByteArray(outLength(src, 0, src.size))
            val ret: Int = decode0(src, 0, src.size, dst)
            if (ret != dst.size) {
                dst = Arrays.copyOf(dst, ret)
            }
            return dst
        }

        /**
         * Decodes a Base64 encoded String into a newly-allocated byte array
         * using the [Base64] encoding scheme.
         *
         *
         *  An invocation of this method has exactly the same effect as invoking
         * `decode(src.getBytes(StandardCharsets.ISO_8859_1))`
         *
         * @param   src
         * the string to decode
         *
         * @return  A newly-allocated byte array containing the decoded bytes.
         *
         * @throws  IllegalArgumentException
         * if `src` is not in valid Base64 scheme
         */
        fun decode(src: String?): ByteArray? {
            return decode(src.getBytes(StandardCharsets.ISO_8859_1))
        }

        /**
         * Decodes all bytes from the input byte array using the [Base64]
         * encoding scheme, writing the results into the given output byte array,
         * starting at offset 0.
         *
         *
         *  It is the responsibility of the invoker of this method to make
         * sure the output byte array `dst` has enough space for decoding
         * all bytes from the input byte array. No bytes will be be written to
         * the output byte array if the output byte array is not big enough.
         *
         *
         *  If the input byte array is not in valid Base64 encoding scheme
         * then some bytes may have been written to the output byte array before
         * IllegalargumentException is thrown.
         *
         * @param   src
         * the byte array to decode
         * @param   dst
         * the output byte array
         *
         * @return  The number of bytes written to the output byte array
         *
         * @throws  IllegalArgumentException
         * if `src` is not in valid Base64 scheme, or `dst`
         * does not have enough space for decoding all input bytes.
         */
        fun decode(src: ByteArray?, dst: ByteArray?): Int {
            val len: Int = outLength(src, 0, src.size)
            if (dst.size < len) throw IllegalArgumentException(
                "Output byte array is too small for decoding all input bytes"
            )
            return decode0(src, 0, src.size, dst)
        }

        /**
         * Decodes all bytes from the input byte buffer using the [Base64]
         * encoding scheme, writing the results into a newly-allocated ByteBuffer.
         *
         *
         *  Upon return, the source buffer's position will be updated to
         * its limit; its limit will not have been changed. The returned
         * output buffer's position will be zero and its limit will be the
         * number of resulting decoded bytes
         *
         *
         *  `IllegalArgumentException` is thrown if the input buffer
         * is not in valid Base64 encoding scheme. The position of the input
         * buffer will not be advanced in this case.
         *
         * @param   buffer
         * the ByteBuffer to decode
         *
         * @return  A newly-allocated byte buffer containing the decoded bytes
         *
         * @throws  IllegalArgumentException
         * if `src` is not in valid Base64 scheme.
         */
        fun decode(buffer: ByteBuffer?): ByteBuffer? {
            val pos0: Int = buffer.position()
            try {
                val src: ByteArray?
                val sp: Int
                val sl: Int
                if (buffer.hasArray()) {
                    src = buffer.array()
                    sp = buffer.arrayOffset() + buffer.position()
                    sl = buffer.arrayOffset() + buffer.limit()
                    buffer.position(buffer.limit())
                } else {
                    src = ByteArray(buffer.remaining())
                    buffer.get(src)
                    sp = 0
                    sl = src.size
                }
                val dst: ByteArray? = ByteArray(outLength(src, sp, sl))
                return ByteBuffer.wrap(dst, 0, decode0(src, sp, sl, dst))
            } catch (iae: IllegalArgumentException) {
                buffer.position(pos0)
                throw iae
            }
        }

        /**
         * Returns an input stream for decoding [Base64] encoded byte stream.
         *
         *
         *  The `read`  methods of the returned `InputStream` will
         * throw `IOException` when reading bytes that cannot be decoded.
         *
         *
         *  Closing the returned input stream will close the underlying
         * input stream.
         *
         * @param   is
         * the input stream
         *
         * @return  the input stream for decoding the specified Base64 encoded
         * byte stream
         */
        fun wrap(`is`: InputStream?): InputStream? {
            Objects.requireNonNull(`is`)
            return DecInputStream(`is`, if (isURL) fromBase64URL else fromBase64, isMIME)
        }

        private fun outLength(src: ByteArray?, sp: Int, sl: Int): Int {
            var sp: Int = sp
            val base64: IntArray? = if (isURL) fromBase64URL else fromBase64
            var paddings: Int = 0
            var len: Int = sl - sp
            if (len == 0) return 0
            if (len < 2) {
                if (isMIME && base64.get(0) == -1) return 0
                throw IllegalArgumentException(
                    "Input byte[] should at least have 2 bytes for base64 bytes"
                )
            }
            if (isMIME) {
                // scan all bytes to fill out all non-alphabet. a performance
                // trade-off of pre-scan or Arrays.copyOf
                var n: Int = 0
                while (sp < sl) {
                    var b: Int = src.get(sp++) and 0xff
                    if (b == '='.toInt()) {
                        len -= (sl - sp + 1)
                        break
                    }
                    if ((base64.get(b).also({ b = it })) == -1) n++
                }
                len -= n
            } else {
                if (src.get(sl - 1) == '=') {
                    paddings++
                    if (src.get(sl - 2) == '=') paddings++
                }
            }
            if (paddings == 0 && (len and 0x3) != 0) paddings = 4 - (len and 0x3)
            return 3 * ((len + 3) / 4) - paddings
        }

        private fun decode0(src: ByteArray?, sp: Int, sl: Int, dst: ByteArray?): Int {
            var sp: Int = sp
            val base64: IntArray? = if (isURL) fromBase64URL else fromBase64
            var dp: Int = 0
            var bits: Int = 0
            var shiftto: Int = 18 // pos of first byte of 4-byte atom
            while (sp < sl) {
                var b: Int = src.get(sp++) and 0xff
                if ((base64.get(b).also({ b = it })) < 0) {
                    if (b == -2) {         // padding byte '='
                        // =     shiftto==18 unnecessary padding
                        // x=    shiftto==12 a dangling single x
                        // x     to be handled together with non-padding case
                        // xx=   shiftto==6&&sp==sl missing last =
                        // xx=y  shiftto==6 last is not =
                        if (shiftto == 6 && (sp == sl || src.get(sp++) != '=') ||
                            shiftto == 18
                        ) {
                            throw IllegalArgumentException(
                                "Input byte array has wrong 4-byte ending unit"
                            )
                        }
                        break
                    }
                    if (isMIME) // skip if for rfc2045
                        continue else throw IllegalArgumentException(
                        "Illegal base64 character " +
                                Integer.toString(src.get(sp - 1), 16)
                    )
                }
                bits = bits or (b shl shiftto)
                shiftto -= 6
                if (shiftto < 0) {
                    dst.get(dp++) = (bits shr 16) as Byte
                    dst.get(dp++) = (bits shr 8) as Byte
                    dst.get(dp++) = (bits) as Byte
                    shiftto = 18
                    bits = 0
                }
            }
            // reached end of byte array or hit padding '=' characters.
            if (shiftto == 6) {
                dst.get(dp++) = (bits shr 16) as Byte
            } else if (shiftto == 0) {
                dst.get(dp++) = (bits shr 16) as Byte
                dst.get(dp++) = (bits shr 8) as Byte
            } else if (shiftto == 12) {
                // dangling single "x", incorrectly encoded.
                throw IllegalArgumentException(
                    "Last unit does not have enough valid bits"
                )
            }
            // anything left is invalid, if is not MIME.
            // if MIME, ignore all non-base64 character
            while (sp < sl) {
                if (isMIME && base64.get(src.get(sp++)) < 0) continue
                throw IllegalArgumentException(
                    "Input byte array has incorrect ending byte at " + sp
                )
            }
            return dp
        }
    }

    /*
     * An output stream for encoding bytes into the Base64.
     */
    private class EncOutputStream internal constructor(
        os: OutputStream?, // byte->base64 mapping
        private val base64: CharArray?,
        // line separator, if needed
        private val newline: ByteArray?, private val linemax: Int, // whether or not to pad
        private val doPadding: Boolean
    ) : FilterOutputStream(os) {
        private var leftover: Int = 0
        private var b0: Int = 0
        private var b1: Int = 0
        private var b2: Int = 0
        private var closed: Boolean = false
        private var linepos: Int = 0
        @Override
        @Throws(IOException::class)
        fun write(b: Int) {
            val buf: ByteArray? = ByteArray(1)
            buf.get(0) = (b and 0xff) as Byte
            write(buf, 0, 1)
        }

        @Throws(IOException::class)
        private fun checkNewline() {
            if (linepos == linemax) {
                out.write(newline)
                linepos = 0
            }
        }

        @Override
        @Throws(IOException::class)
        fun write(b: ByteArray?, off: Int, len: Int) {
            var off: Int = off
            var len: Int = len
            if (closed) throw IOException("Stream is closed")
            if ((off < 0) || (len < 0) || (len > b.size - off)) throw ArrayIndexOutOfBoundsException()
            if (len == 0) return
            if (leftover != 0) {
                if (leftover == 1) {
                    b1 = b.get(off++) and 0xff
                    len--
                    if (len == 0) {
                        leftover++
                        return
                    }
                }
                b2 = b.get(off++) and 0xff
                len--
                checkNewline()
                out.write(base64.get(b0 shr 2))
                out.write(base64.get((b0 shl 4) and 0x3f or (b1 shr 4)))
                out.write(base64.get((b1 shl 2) and 0x3f or (b2 shr 6)))
                out.write(base64.get(b2 and 0x3f))
                linepos += 4
            }
            var nBits24: Int = len / 3
            leftover = len - (nBits24 * 3)
            while (nBits24-- > 0) {
                checkNewline()
                val bits: Int = (((b.get(off++) and 0xff) shl 16) or (
                        (b.get(off++) and 0xff) shl 8) or
                        (b.get(off++) and 0xff))
                out.write(base64.get((bits ushr 18) and 0x3f))
                out.write(base64.get((bits ushr 12) and 0x3f))
                out.write(base64.get((bits ushr 6) and 0x3f))
                out.write(base64.get(bits and 0x3f))
                linepos += 4
            }
            if (leftover == 1) {
                b0 = b.get(off++) and 0xff
            } else if (leftover == 2) {
                b0 = b.get(off++) and 0xff
                b1 = b.get(off++) and 0xff
            }
        }

        @Override
        @Throws(IOException::class)
        fun close() {
            if (!closed) {
                closed = true
                if (leftover == 1) {
                    checkNewline()
                    out.write(base64.get(b0 shr 2))
                    out.write(base64.get((b0 shl 4) and 0x3f))
                    if (doPadding) {
                        out.write('=')
                        out.write('=')
                    }
                } else if (leftover == 2) {
                    checkNewline()
                    out.write(base64.get(b0 shr 2))
                    out.write(base64.get((b0 shl 4) and 0x3f or (b1 shr 4)))
                    out.write(base64.get((b1 shl 2) and 0x3f))
                    if (doPadding) {
                        out.write('=')
                    }
                }
                leftover = 0
                out.close()
            }
        }
    }

    /*
     * An input stream for decoding Base64 bytes
     */
    private class DecInputStream internal constructor(
        `is`: InputStream?,
        base64: IntArray?,
        isMIME: Boolean
    ) : InputStream() {
        private val `is`: InputStream?
        private val isMIME: Boolean
        private val base64 // base64 -> byte mapping
                : IntArray?
        private var bits: Int = 0 // 24-bit buffer for decoding
        private var nextin: Int = 18 // next available "off" in "bits" for input;

        // -> 18, 12, 6, 0
        private var nextout: Int = -8 // next available "off" in "bits" for output;

        // -> 8, 0, -8 (no byte for output)
        private var eof: Boolean = false
        private var closed: Boolean = false
        private val sbBuf: ByteArray? = ByteArray(1)
        @Override
        @Throws(IOException::class)
        fun read(): Int {
            return if (read(sbBuf, 0, 1) == -1) -1 else sbBuf.get(0) and 0xff
        }

        @Override
        @Throws(IOException::class)
        fun read(b: ByteArray?, off: Int, len: Int): Int {
            var off: Int = off
            var len: Int = len
            if (closed) throw IOException("Stream is closed")
            if (eof && nextout < 0) // eof and no leftover
                return -1
            if ((off < 0) || (len < 0) || (len > b.size - off)) throw IndexOutOfBoundsException()
            val oldOff: Int = off
            if (nextout >= 0) {       // leftover output byte(s) in bits buf
                do {
                    if (len == 0) return off - oldOff
                    b.get(off++) = (bits shr nextout) as Byte
                    len--
                    nextout -= 8
                } while (nextout >= 0)
                bits = 0
            }
            while (len > 0) {
                var v: Int = `is`.read()
                if (v == -1) {
                    eof = true
                    if (nextin != 18) {
                        if (nextin == 12) throw IOException("Base64 stream has one un-decoded dangling byte.")
                        // treat ending xx/xxx without padding character legal.
                        // same logic as v == '=' below
                        b.get(off++) = (bits shr (16)) as Byte
                        len--
                        if (nextin == 0) {           // only one padding byte
                            if (len == 0) {          // no enough output space
                                bits = bits shr 8 // shift to lowest byte
                                nextout = 0
                            } else {
                                b.get(off++) = (bits shr 8) as Byte
                            }
                        }
                    }
                    if (off == oldOff) return -1 else return off - oldOff
                }
                if (v == '='.toInt()) {                  // padding byte(s)
                    // =     shiftto==18 unnecessary padding
                    // x=    shiftto==12 dangling x, invalid unit
                    // xx=   shiftto==6 && missing last '='
                    // xx=y  or last is not '='
                    if ((nextin == 18) || (nextin == 12) || (
                                nextin == 6 && `is`.read() !== '=')
                    ) {
                        throw IOException("Illegal base64 ending sequence:" + nextin)
                    }
                    b.get(off++) = (bits shr (16)) as Byte
                    len--
                    if (nextin == 0) {           // only one padding byte
                        if (len == 0) {          // no enough output space
                            bits = bits shr 8 // shift to lowest byte
                            nextout = 0
                        } else {
                            b.get(off++) = (bits shr 8) as Byte
                        }
                    }
                    eof = true
                    break
                }
                if ((base64.get(v).also({ v = it })) == -1) {
                    if (isMIME) // skip if for rfc2045
                        continue else throw IOException(
                        "Illegal base64 character " +
                                Integer.toString(v, 16)
                    )
                }
                bits = bits or (v shl nextin)
                if (nextin == 0) {
                    nextin = 18 // clear for next
                    nextout = 16
                    while (nextout >= 0) {
                        b.get(off++) = (bits shr nextout) as Byte
                        len--
                        nextout -= 8
                        if (len == 0 && nextout >= 0) {  // don't clean "bits"
                            return off - oldOff
                        }
                    }
                    bits = 0
                } else {
                    nextin -= 6
                }
            }
            return off - oldOff
        }

        @Override
        @Throws(IOException::class)
        fun available(): Int {
            if (closed) throw IOException("Stream is closed")
            return `is`.available() // TBD:
        }

        @Override
        @Throws(IOException::class)
        fun close() {
            if (!closed) {
                closed = true
                `is`.close()
            }
        }

        init {
            this.`is` = `is`
            this.base64 = base64
            this.isMIME = isMIME
        }
    }
}