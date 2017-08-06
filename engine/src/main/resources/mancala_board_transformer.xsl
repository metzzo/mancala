<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="num_stones" />

    <xsl:template name="position">
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
    </xsl:template>

    <xsl:template match="player-depot">
        <BorderPane xmlns:fx="http://javafx.com/fxml">
            <xsl:call-template name="position" />

            <xsl:attribute name="id">
                <xsl:value-of select="@id" />
            </xsl:attribute>

            <xsl:if test="@player = 0">
                <bottom>
                    <Label text="Player 1" styleClass="player-label" fx:id="depotLabel0"/>
                </bottom>
            </xsl:if>
            <xsl:if test="@player = 1">
                <top>
                    <Label text="Player 2" styleClass="player-label" fx:id="depotLabel1" />
                </top>
            </xsl:if>

            <center>
                <Button disable="true">
                    <xsl:attribute name="text">0</xsl:attribute>
                </Button>
            </center>
        </BorderPane>
    </xsl:template>

    <xsl:template match="slot">
        <Button onAction="#handleAction">
            <xsl:call-template name="position" />

            <xsl:attribute name="id">
                <xsl:value-of select="@id" />
            </xsl:attribute>

            <xsl:attribute name="text">
                <xsl:value-of select="$num_stones" />
            </xsl:attribute>
        </Button>
    </xsl:template>

    <xsl:template match="board">
        <AnchorPane xmlns="http://javafx.com/javafx"
                    xmlns:fx="http://javafx.com/fxml"
                    fx:id="root"
                    fx:controller="at.pwd.boardgame.controller.BoardController">
            <GridPane alignment="center"
                      hgap="10"
                      vgap="10"
                      prefHeight="400.0"
                      prefWidth="600.0"
                      fx:id="grid">
                <xsl:apply-templates />
            </GridPane>

            <stylesheets>
                <URL value="@style.css" />
            </stylesheets>
        </AnchorPane>
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

        <xsl:processing-instruction name="import">
            java.net.*
        </xsl:processing-instruction>

        <xsl:apply-templates />
    </xsl:template>

</xsl:stylesheet>