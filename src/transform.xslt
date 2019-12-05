<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match = "/">
        export const transactions = [
            <xsl:for-each select="table/tbody/tr">
                <xsl:if test="child::node()[contains(@class,'expander_handle_column_1')]">
                {
                    <xsl:for-each select="child::node()[contains(@class,'expander_handle_column_1')]">
                        source: '<xsl:value-of select="."/>',
                    </xsl:for-each>
                    <xsl:for-each select="child::node()[contains(@class,'expander_handle_column_6')]">
                        amount: '<xsl:value-of select="."/>'
                    </xsl:for-each>
                },
                </xsl:if>
            </xsl:for-each>
        ];
    </xsl:template>
</xsl:stylesheet>
