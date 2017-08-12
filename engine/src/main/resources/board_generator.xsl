<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="num_stones" />
    <xsl:param name="computation_time" />
    <xsl:param name="slots_per_player" />

    <xsl:template name="slot_generator">
        <xsl:param name="var"/>
        <xsl:choose>
            <xsl:when test="$var >= 0">
                <slot>
                    <xsl:attribute name="id">
                        <xsl:value-of select="$var + 1" />
                    </xsl:attribute>
                    <xsl:attribute name="next">
                        <xsl:value-of select="$var" />
                    </xsl:attribute>
                    <xsl:attribute name="row">
                        0
                    </xsl:attribute>
                    <xsl:attribute name="column">
                        <xsl:value-of select="$var" />
                    </xsl:attribute>
                    <xsl:attribute name="belongs">
                        1
                    </xsl:attribute>
                    <xsl:attribute name="enemy">
                        <xsl:value-of select="$slots_per_player + 3 + ($slots_per_player - $var)" />
                    </xsl:attribute>
                </slot>
                <slot>
                    <xsl:attribute name="id">
                        <xsl:value-of select="$var + $slots_per_player + 2" />
                    </xsl:attribute>
                    <xsl:attribute name="next">
                        <xsl:value-of select="$var + $slots_per_player + 1" />
                    </xsl:attribute>
                    <xsl:attribute name="row">
                        1
                    </xsl:attribute>
                    <xsl:attribute name="column">
                        <xsl:value-of select="$var - $slots_per_player + 1" />
                    </xsl:attribute>
                    <xsl:attribute name="belongs">
                        0
                    </xsl:attribute>
                    <xsl:attribute name="enemy">
                        <xsl:value-of select="$var - $slots_per_player + 2" />
                    </xsl:attribute>
                </slot>

                <xsl:call-template name="slot_generator">
                    <xsl:with-param name="var" select="$var - 1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise/>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="/">
        <board>
            <xsl:attribute name="numstones">
                <xsl:value-of select="$num_stones" />
            </xsl:attribute>
            <xsl:call-template name="slot_generator">
                <xsl:with-param name="var" select="$slots_per_player" />
            </xsl:call-template>
        </board>
    </xsl:template>

</xsl:stylesheet>