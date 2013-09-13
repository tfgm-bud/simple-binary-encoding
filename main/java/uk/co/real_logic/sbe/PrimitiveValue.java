/* -*- mode: java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil -*- */
/*
 * Copyright 2013 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.sbe;

/**
 * Class used to encapsulate values for primitives. Used for nullValue, minValue, maxValue, and constants
 */
public class PrimitiveValue
{
    /**
     * Representation of value is a Java long
     */
    private static final int LONG_VALUE_REPRESENTATION = 0x1;

    /**
     * SBE primitiveType null/min/max value default constants
     * <p/>
     * Primitive type	Null        Min          Max
     * char             0           0x20         0x7E
     * int8	        -128        -127         127
     * uint8	        255         0            254
     * int16	        -32768      -32767       32767
     * uint16	        65535       0            65534
     * int32	        2^31        -2^31 + 1    2^31 - 1
     * uint32	        2^32 - 1    0            2^32 - 2
     * int64	        2^63        -2^63 + 1    2^63 - 1
     * uint64	        2^64 - 1    0            2^64 - 2
     */
    private static final long MIN_VALUE_CHAR = 0x20;
    private static final long MAX_VALUE_CHAR = 0x7E;
    private static final long NULL_VALUE_CHAR = 0;

    private static final long MIN_VALUE_INT8 = -127;
    private static final long MAX_VALUE_INT8 = 127;
    private static final long NULL_VALUE_INT8 = -128;

    private static final long MIN_VALUE_UINT8 = 0;
    private static final long MAX_VALUE_UINT8 = 254;
    private static final long NULL_VALUE_UINT8 = 255;

    private static final long MIN_VALUE_INT16 = -32767;
    private static final long MAX_VALUE_INT16 = 32767;
    private static final long NULL_VALUE_INT16 = -32768;

    private static final long MIN_VALUE_UINT16 = 0;
    private static final long MAX_VALUE_UINT16 = 65534;
    private static final long NULL_VALUE_UINT16 = 65535;

    private static final long MIN_VALUE_INT32 = -2147483647;
    private static final long MAX_VALUE_INT32 = 2147483647;
    private static final long NULL_VALUE_INT32 = -2147483648;

    private static final long MIN_VALUE_UINT32 = 0;
    private static final long MAX_VALUE_UINT32 = 0xFFFFFFFD;
    private static final long NULL_VALUE_UINT32 = 0xFFFFFFFE;

    private static final long MIN_VALUE_INT64 = Long.MIN_VALUE + 1;  // -2^63 + 1
    private static final long MAX_VALUE_INT64 = Long.MAX_VALUE;      //  2^63 - 1  (SBE spec says -12^63 - 1)
    private static final long NULL_VALUE_INT64 = Long.MIN_VALUE;     // -2^63

    private static final long MIN_VALUE_UINT64 = 0;
    private static final long MAX_VALUE_UINT64 = Long.MAX_VALUE;  // TODO: placeholder for now (replace with BigInteger?)
    private static final long NULL_VALUE_UINT64 = Long.MIN_VALUE; // TODO: placeholder for now (replace with BigInteger?)

    /**
     * value expressed as a long
     * Java max = 2^63 - 1
     * Java min = -2^63
     */
    private final long longValue;

    /**
     * Representation of the value internally to this class
     */
    private final int representation;

    /**
     * Construct and fill in value as a long
     *
     * @param value 
     */
    public PrimitiveValue(final long value)
    {
	this.longValue = value;
	this.representation = LONG_VALUE_REPRESENTATION;
    }

    /**
     * Parse constant value string and return constructed value
     *
     * @param primitive that this is supposed to be
     * @param value     expressed as a String
     * @return long representation of the value
     * @throws IllegalArgumentException if parsing not known for type
     */
    public PrimitiveValue(final Primitive primitive, final String value)
    {
	this.longValue = parseConstValue2Long(primitive, value);
	this.representation = LONG_VALUE_REPRESENTATION;
    }

    /**
     * Return string reresentaiton of this object
     *
     * @return String representing object value
     * @throws IllegalArgumentException if unknown representation
     */
    public String toString()
    {
	switch (representation)
	{
	    case LONG_VALUE_REPRESENTATION:
		return Long.toString(longValue);
	    default:
		throw new IllegalArgumentException("unknown PrimitiveValue representation");
	}
    }

    /**
     * Determine if two values are equivalent
     *
     * @param value to compare this value with
     * @return equivalence of values
     */
    public boolean equals(Object value)
    {
	if (value instanceof PrimitiveValue)
	{
	    PrimitiveValue lhs = (PrimitiveValue)value;

	    if (lhs.representation == this.representation &&
		lhs.longValue == this.longValue)
	    {
		return true;
	    }
	}
	return false;
    }

    /**
     * Parse constant value string and return long representation
     *
     * @param primitive that this is supposed to be
     * @param value     expressed as a String
     * @return long representation of the value
     * @throws IllegalArgumentException if parsing not known for type
     */
    public static long parseConstValue2Long(final Primitive primitive, final String value)
    {
        switch (primitive)
        {
            case CHAR:
                if (value.length() > 1)
                {
                    throw new IllegalArgumentException("constant char value malformed");
                }
                return value.getBytes()[0];

            case INT8:
            case INT16:
            case INT32:
            case INT64:
            case UINT8:
            case UINT16:
            case UINT32:
            case UINT64:
                /**
                 * TODO: not entirely adequate, but then again, Java doesn't have unsigned 64-bit ints...
                 */
                return Long.parseLong(value);

            default:
                throw new IllegalArgumentException("Do not know how to parse this primitive type for constant value");
        }
    }

}