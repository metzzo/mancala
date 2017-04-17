<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="num_stones" />

    <xsl:template match="player-depot|slot">
        <Button onAction="#handleAction">
            <xsl:attribute name="id">
                <xsl:value-of select="@id" />
            </xsl:attribute>

            <xsl:attribute name="text">
                <xsl:choose>
                    <xsl:when test="name() = 'player-depot'">0</xsl:when>
                    <xsl:when test="name() = 'slot'">
                        <xsl:value-of select="$num_stones" />
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>

            <xsl:attribute name="GridPane.columnIndex">
                <xsl:choose>
                    <xsl:when test="@column">
                        <xsl:value-of select="@column" />
                    </xsl:when>
                    <xsl:otherwise>1</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="GridPane.rowIndex">
                <xsl:choose>
                    <xsl:when test="@row">
                        <xsl:value-of select="@row" />
                    </xsl:when>
                    <xsl:otherwise>1</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="GridPane.columnSpan">
                <xsl:choose>
                    <xsl:when test="@columnspan">
                        <xsl:value-of select="@columnspan" />
                    </xsl:when>
                    <xsl:otherwise>1</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:attribute name="GridPane.rowSpan">
                <xsl:choose>
                    <xsl:when test="@rowspan">
                        <xsl:value-of select="@rowspan" />
                    </xsl:when>
                    <xsl:otherwise>1</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </Button>
    </xsl:template>

    <xsl:template match="board">
        <GridPane xmlns="http://javafx.com/javafx"
                  xmlns:fx="http://javafx.com/fxml"
                  fx:controller="at.pwd.boardgame.game.mancala.MancalaBoardController"
                  prefHeight="400.0"
                  prefWidth="600.0"
                  alignment="center"
                  hgap="10"
                  vgap="10">

            <xsl:apply-templates />

        </GridPane>
    </xsl:template>

    <xsl:template match="/">
        <xsl:processing-instruction name="import">
            java.lang.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            java.util.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.layout.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.control.*
        </xsl:processing-instruction>

        <xsl:apply-templates />
    </xsl:template>

</xsl:stylesheet>