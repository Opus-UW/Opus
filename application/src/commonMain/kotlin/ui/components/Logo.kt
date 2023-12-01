package ui.components
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


@Composable
fun lightModeLogoVector(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "title-light",
            defaultWidth = 537.dp,
            defaultHeight = 146.dp,
            viewportWidth = 537f,
            viewportHeight = 146f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF232323)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(188.296f, 58.664f)
                curveTo(188.296f, 52.264f, 189.491f, 46.2053f, 191.88f, 40.488f)
                curveTo(194.355f, 34.7707f, 197.768f, 29.6933f, 202.12f, 25.256f)
                curveTo(206.472f, 20.8187f, 211.507f, 17.3627f, 217.224f, 14.888f)
                curveTo(222.941f, 12.328f, 229.085f, 11.048f, 235.656f, 11.048f)
                curveTo(242.141f, 11.048f, 248.243f, 12.328f, 253.96f, 14.888f)
                curveTo(259.677f, 17.3627f, 264.755f, 20.8187f, 269.192f, 25.256f)
                curveTo(273.629f, 29.6933f, 277.085f, 34.7707f, 279.56f, 40.488f)
                curveTo(282.035f, 46.2053f, 283.272f, 52.264f, 283.272f, 58.664f)
                curveTo(283.272f, 65.2347f, 282.035f, 71.3787f, 279.56f, 77.096f)
                curveTo(277.085f, 82.8133f, 273.629f, 87.848f, 269.192f, 92.2f)
                curveTo(264.84f, 96.552f, 259.763f, 99.9653f, 253.96f, 102.44f)
                curveTo(248.243f, 104.829f, 242.141f, 106.024f, 235.656f, 106.024f)
                curveTo(229.085f, 106.024f, 222.941f, 104.829f, 217.224f, 102.44f)
                curveTo(211.507f, 100.051f, 206.472f, 96.7227f, 202.12f, 92.456f)
                curveTo(197.768f, 88.104f, 194.355f, 83.0693f, 191.88f, 77.352f)
                curveTo(189.491f, 71.5493f, 188.296f, 65.32f, 188.296f, 58.664f)
                close()
                moveTo(202.376f, 58.664f)
                curveTo(202.376f, 63.4427f, 203.229f, 67.9227f, 204.936f, 72.104f)
                curveTo(206.728f, 76.2f, 209.16f, 79.8267f, 212.232f, 82.984f)
                curveTo(215.304f, 86.056f, 218.845f, 88.488f, 222.856f, 90.28f)
                curveTo(226.952f, 91.9867f, 231.347f, 92.84f, 236.04f, 92.84f)
                curveTo(240.648f, 92.84f, 244.957f, 91.9867f, 248.968f, 90.28f)
                curveTo(252.979f, 88.488f, 256.477f, 86.056f, 259.464f, 82.984f)
                curveTo(262.536f, 79.8267f, 264.925f, 76.2f, 266.632f, 72.104f)
                curveTo(268.339f, 67.9227f, 269.192f, 63.4427f, 269.192f, 58.664f)
                curveTo(269.192f, 53.8853f, 268.296f, 49.448f, 266.504f, 45.352f)
                curveTo(264.797f, 41.1707f, 262.408f, 37.5013f, 259.336f, 34.344f)
                curveTo(256.349f, 31.1867f, 252.808f, 28.712f, 248.712f, 26.92f)
                curveTo(244.701f, 25.128f, 240.392f, 24.232f, 235.784f, 24.232f)
                curveTo(231.005f, 24.232f, 226.611f, 25.128f, 222.6f, 26.92f)
                curveTo(218.589f, 28.712f, 215.048f, 31.1867f, 211.976f, 34.344f)
                curveTo(208.904f, 37.5013f, 206.515f, 41.1707f, 204.808f, 45.352f)
                curveTo(203.187f, 49.5333f, 202.376f, 53.9707f, 202.376f, 58.664f)
                close()
                moveTo(329.281f, 12.072f)
                curveTo(336.193f, 12.072f, 342.081f, 13.2667f, 346.945f, 15.656f)
                curveTo(351.894f, 17.96f, 355.649f, 21.288f, 358.209f, 25.64f)
                curveTo(360.854f, 29.992f, 362.177f, 35.2827f, 362.177f, 41.512f)
                curveTo(362.177f, 45.1813f, 361.622f, 48.808f, 360.513f, 52.392f)
                curveTo(359.404f, 55.976f, 357.612f, 59.2613f, 355.137f, 62.248f)
                curveTo(352.748f, 65.1493f, 349.548f, 67.496f, 345.537f, 69.288f)
                curveTo(341.526f, 70.9947f, 336.662f, 71.848f, 330.945f, 71.848f)
                horizontalLineTo(317.377f)
                verticalLineTo(105f)
                horizontalLineTo(303.681f)
                verticalLineTo(12.072f)
                horizontalLineTo(329.281f)
                close()
                moveTo(330.945f, 58.664f)
                curveTo(334.358f, 58.664f, 337.217f, 58.1093f, 339.521f, 57f)
                curveTo(341.825f, 55.8053f, 343.617f, 54.312f, 344.897f, 52.52f)
                curveTo(346.262f, 50.728f, 347.201f, 48.8933f, 347.713f, 47.016f)
                curveTo(348.31f, 45.0533f, 348.609f, 43.304f, 348.609f, 41.768f)
                curveTo(348.609f, 40.0613f, 348.31f, 38.2693f, 347.713f, 36.392f)
                curveTo(347.201f, 34.4293f, 346.305f, 32.6373f, 345.025f, 31.016f)
                curveTo(343.745f, 29.3093f, 341.996f, 27.944f, 339.777f, 26.92f)
                curveTo(337.558f, 25.8107f, 334.785f, 25.256f, 331.457f, 25.256f)
                horizontalLineTo(317.377f)
                verticalLineTo(58.664f)
                horizontalLineTo(330.945f)
                close()
                moveTo(393.094f, 71.848f)
                curveTo(393.094f, 75.7733f, 394.075f, 79.3147f, 396.038f, 82.472f)
                curveTo(398.001f, 85.6293f, 400.603f, 88.1467f, 403.846f, 90.024f)
                curveTo(407.089f, 91.9013f, 410.715f, 92.84f, 414.726f, 92.84f)
                curveTo(418.993f, 92.84f, 422.79f, 91.9013f, 426.118f, 90.024f)
                curveTo(429.446f, 88.1467f, 432.049f, 85.6293f, 433.926f, 82.472f)
                curveTo(435.889f, 79.3147f, 436.87f, 75.7733f, 436.87f, 71.848f)
                verticalLineTo(12.072f)
                horizontalLineTo(450.31f)
                verticalLineTo(72.232f)
                curveTo(450.31f, 78.9733f, 448.731f, 84.904f, 445.574f, 90.024f)
                curveTo(442.417f, 95.0587f, 438.15f, 98.984f, 432.774f, 101.8f)
                curveTo(427.398f, 104.616f, 421.382f, 106.024f, 414.726f, 106.024f)
                curveTo(408.241f, 106.024f, 402.31f, 104.616f, 396.934f, 101.8f)
                curveTo(391.558f, 98.984f, 387.291f, 95.0587f, 384.134f, 90.024f)
                curveTo(381.062f, 84.904f, 379.526f, 78.9733f, 379.526f, 72.232f)
                verticalLineTo(12.072f)
                horizontalLineTo(393.094f)
                verticalLineTo(71.848f)
                close()
                moveTo(523.44f, 29.864f)
                curveTo(520.197f, 28.1573f, 516.699f, 26.7067f, 512.944f, 25.512f)
                curveTo(509.189f, 24.3173f, 505.563f, 23.72f, 502.064f, 23.72f)
                curveTo(497.285f, 23.72f, 493.488f, 24.8293f, 490.672f, 27.048f)
                curveTo(487.856f, 29.1813f, 486.448f, 32.1253f, 486.448f, 35.88f)
                curveTo(486.448f, 38.5253f, 487.387f, 40.8293f, 489.264f, 42.792f)
                curveTo(491.141f, 44.7547f, 493.573f, 46.4613f, 496.56f, 47.912f)
                curveTo(499.547f, 49.3627f, 502.704f, 50.728f, 506.032f, 52.008f)
                curveTo(508.848f, 53.032f, 511.621f, 54.2693f, 514.352f, 55.72f)
                curveTo(517.168f, 57.0853f, 519.685f, 58.792f, 521.904f, 60.84f)
                curveTo(524.208f, 62.888f, 526f, 65.448f, 527.28f, 68.52f)
                curveTo(528.645f, 71.5067f, 529.328f, 75.176f, 529.328f, 79.528f)
                curveTo(529.328f, 84.4773f, 528.091f, 88.9573f, 525.616f, 92.968f)
                curveTo(523.227f, 96.9787f, 519.728f, 100.179f, 515.12f, 102.568f)
                curveTo(510.597f, 104.872f, 505.093f, 106.024f, 498.608f, 106.024f)
                curveTo(494.683f, 106.024f, 490.843f, 105.597f, 487.088f, 104.744f)
                curveTo(483.419f, 103.805f, 479.92f, 102.611f, 476.592f, 101.16f)
                curveTo(473.349f, 99.624f, 470.405f, 97.96f, 467.76f, 96.168f)
                lineTo(473.904f, 85.544f)
                curveTo(475.952f, 86.9947f, 478.299f, 88.4027f, 480.944f, 89.768f)
                curveTo(483.589f, 91.048f, 486.32f, 92.072f, 489.136f, 92.84f)
                curveTo(492.037f, 93.608f, 494.768f, 93.992f, 497.328f, 93.992f)
                curveTo(500.144f, 93.992f, 502.917f, 93.5227f, 505.648f, 92.584f)
                curveTo(508.379f, 91.56f, 510.64f, 90.024f, 512.432f, 87.976f)
                curveTo(514.224f, 85.8427f, 515.12f, 83.0693f, 515.12f, 79.656f)
                curveTo(515.12f, 76.7547f, 514.309f, 74.3227f, 512.688f, 72.36f)
                curveTo(511.152f, 70.312f, 509.061f, 68.5627f, 506.416f, 67.112f)
                curveTo(503.856f, 65.6613f, 501.083f, 64.3813f, 498.096f, 63.272f)
                curveTo(495.195f, 62.1627f, 492.251f, 60.9253f, 489.264f, 59.56f)
                curveTo(486.277f, 58.1947f, 483.504f, 56.5733f, 480.944f, 54.696f)
                curveTo(478.384f, 52.7333f, 476.293f, 50.344f, 474.672f, 47.528f)
                curveTo(473.136f, 44.6267f, 472.368f, 41.0853f, 472.368f, 36.904f)
                curveTo(472.368f, 31.9547f, 473.52f, 27.6453f, 475.824f, 23.976f)
                curveTo(478.213f, 20.2213f, 481.499f, 17.2773f, 485.68f, 15.144f)
                curveTo(489.861f, 13.0107f, 494.683f, 11.816f, 500.144f, 11.56f)
                curveTo(506.373f, 11.56f, 511.792f, 12.328f, 516.4f, 13.864f)
                curveTo(521.008f, 15.4f, 525.104f, 17.2347f, 528.688f, 19.368f)
                lineTo(523.44f, 29.864f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF2998FF)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(44.9999f, 16.6483f)
                lineTo(129f, 16.6483f)
            }
            path(
                fill = SolidColor(Color(0xFF2998FF)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(30f, 17.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.5f, 30f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 17.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30f, 17.5f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFFFFC93E)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(58.9999f, 60.4175f)
                lineTo(143f, 60.4175f)
            }
            path(
                fill = SolidColor(Color(0xFFFFC93E)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(44f, 61.5f)
                curveTo(44f, 68.4036f, 38.4036f, 74f, 31.5f, 74f)
                curveTo(24.5964f, 74f, 19f, 68.4036f, 19f, 61.5f)
                curveTo(19f, 54.5964f, 24.5964f, 49f, 31.5f, 49f)
                curveTo(38.4036f, 49f, 44f, 54.5964f, 44f, 61.5f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFFFF295D)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(45f, 104.648f)
                lineTo(129f, 104.648f)
            }
            path(
                fill = SolidColor(Color(0xFFFF295D)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(30f, 105.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.5f, 118f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 105.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30f, 105.5f)
                close()
            }
        }.build()
    }
}

@Composable
fun darkModeLogoVector(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "title-dark",
            defaultWidth = 537.dp,
            defaultHeight = 146.dp,
            viewportWidth = 537f,
            viewportHeight = 146f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFF1F1F1)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(188.296f, 58.664f)
                curveTo(188.296f, 52.264f, 189.491f, 46.2053f, 191.88f, 40.488f)
                curveTo(194.355f, 34.7707f, 197.768f, 29.6933f, 202.12f, 25.256f)
                curveTo(206.472f, 20.8187f, 211.507f, 17.3627f, 217.224f, 14.888f)
                curveTo(222.941f, 12.328f, 229.085f, 11.048f, 235.656f, 11.048f)
                curveTo(242.141f, 11.048f, 248.243f, 12.328f, 253.96f, 14.888f)
                curveTo(259.677f, 17.3627f, 264.755f, 20.8187f, 269.192f, 25.256f)
                curveTo(273.629f, 29.6933f, 277.085f, 34.7707f, 279.56f, 40.488f)
                curveTo(282.035f, 46.2053f, 283.272f, 52.264f, 283.272f, 58.664f)
                curveTo(283.272f, 65.2347f, 282.035f, 71.3787f, 279.56f, 77.096f)
                curveTo(277.085f, 82.8133f, 273.629f, 87.848f, 269.192f, 92.2f)
                curveTo(264.84f, 96.552f, 259.763f, 99.9653f, 253.96f, 102.44f)
                curveTo(248.243f, 104.829f, 242.141f, 106.024f, 235.656f, 106.024f)
                curveTo(229.085f, 106.024f, 222.941f, 104.829f, 217.224f, 102.44f)
                curveTo(211.507f, 100.051f, 206.472f, 96.7227f, 202.12f, 92.456f)
                curveTo(197.768f, 88.104f, 194.355f, 83.0693f, 191.88f, 77.352f)
                curveTo(189.491f, 71.5493f, 188.296f, 65.32f, 188.296f, 58.664f)
                close()
                moveTo(202.376f, 58.664f)
                curveTo(202.376f, 63.4427f, 203.229f, 67.9227f, 204.936f, 72.104f)
                curveTo(206.728f, 76.2f, 209.16f, 79.8267f, 212.232f, 82.984f)
                curveTo(215.304f, 86.056f, 218.845f, 88.488f, 222.856f, 90.28f)
                curveTo(226.952f, 91.9867f, 231.347f, 92.84f, 236.04f, 92.84f)
                curveTo(240.648f, 92.84f, 244.957f, 91.9867f, 248.968f, 90.28f)
                curveTo(252.979f, 88.488f, 256.477f, 86.056f, 259.464f, 82.984f)
                curveTo(262.536f, 79.8267f, 264.925f, 76.2f, 266.632f, 72.104f)
                curveTo(268.339f, 67.9227f, 269.192f, 63.4427f, 269.192f, 58.664f)
                curveTo(269.192f, 53.8853f, 268.296f, 49.448f, 266.504f, 45.352f)
                curveTo(264.797f, 41.1707f, 262.408f, 37.5013f, 259.336f, 34.344f)
                curveTo(256.349f, 31.1867f, 252.808f, 28.712f, 248.712f, 26.92f)
                curveTo(244.701f, 25.128f, 240.392f, 24.232f, 235.784f, 24.232f)
                curveTo(231.005f, 24.232f, 226.611f, 25.128f, 222.6f, 26.92f)
                curveTo(218.589f, 28.712f, 215.048f, 31.1867f, 211.976f, 34.344f)
                curveTo(208.904f, 37.5013f, 206.515f, 41.1707f, 204.808f, 45.352f)
                curveTo(203.187f, 49.5333f, 202.376f, 53.9707f, 202.376f, 58.664f)
                close()
                moveTo(329.281f, 12.072f)
                curveTo(336.193f, 12.072f, 342.081f, 13.2667f, 346.945f, 15.656f)
                curveTo(351.894f, 17.96f, 355.649f, 21.288f, 358.209f, 25.64f)
                curveTo(360.854f, 29.992f, 362.177f, 35.2827f, 362.177f, 41.512f)
                curveTo(362.177f, 45.1813f, 361.622f, 48.808f, 360.513f, 52.392f)
                curveTo(359.404f, 55.976f, 357.612f, 59.2613f, 355.137f, 62.248f)
                curveTo(352.748f, 65.1493f, 349.548f, 67.496f, 345.537f, 69.288f)
                curveTo(341.526f, 70.9947f, 336.662f, 71.848f, 330.945f, 71.848f)
                horizontalLineTo(317.377f)
                verticalLineTo(105f)
                horizontalLineTo(303.681f)
                verticalLineTo(12.072f)
                horizontalLineTo(329.281f)
                close()
                moveTo(330.945f, 58.664f)
                curveTo(334.358f, 58.664f, 337.217f, 58.1093f, 339.521f, 57f)
                curveTo(341.825f, 55.8053f, 343.617f, 54.312f, 344.897f, 52.52f)
                curveTo(346.262f, 50.728f, 347.201f, 48.8933f, 347.713f, 47.016f)
                curveTo(348.31f, 45.0533f, 348.609f, 43.304f, 348.609f, 41.768f)
                curveTo(348.609f, 40.0613f, 348.31f, 38.2693f, 347.713f, 36.392f)
                curveTo(347.201f, 34.4293f, 346.305f, 32.6373f, 345.025f, 31.016f)
                curveTo(343.745f, 29.3093f, 341.996f, 27.944f, 339.777f, 26.92f)
                curveTo(337.558f, 25.8107f, 334.785f, 25.256f, 331.457f, 25.256f)
                horizontalLineTo(317.377f)
                verticalLineTo(58.664f)
                horizontalLineTo(330.945f)
                close()
                moveTo(393.094f, 71.848f)
                curveTo(393.094f, 75.7733f, 394.075f, 79.3147f, 396.038f, 82.472f)
                curveTo(398.001f, 85.6293f, 400.603f, 88.1467f, 403.846f, 90.024f)
                curveTo(407.089f, 91.9013f, 410.715f, 92.84f, 414.726f, 92.84f)
                curveTo(418.993f, 92.84f, 422.79f, 91.9013f, 426.118f, 90.024f)
                curveTo(429.446f, 88.1467f, 432.049f, 85.6293f, 433.926f, 82.472f)
                curveTo(435.889f, 79.3147f, 436.87f, 75.7733f, 436.87f, 71.848f)
                verticalLineTo(12.072f)
                horizontalLineTo(450.31f)
                verticalLineTo(72.232f)
                curveTo(450.31f, 78.9733f, 448.731f, 84.904f, 445.574f, 90.024f)
                curveTo(442.417f, 95.0587f, 438.15f, 98.984f, 432.774f, 101.8f)
                curveTo(427.398f, 104.616f, 421.382f, 106.024f, 414.726f, 106.024f)
                curveTo(408.241f, 106.024f, 402.31f, 104.616f, 396.934f, 101.8f)
                curveTo(391.558f, 98.984f, 387.291f, 95.0587f, 384.134f, 90.024f)
                curveTo(381.062f, 84.904f, 379.526f, 78.9733f, 379.526f, 72.232f)
                verticalLineTo(12.072f)
                horizontalLineTo(393.094f)
                verticalLineTo(71.848f)
                close()
                moveTo(523.44f, 29.864f)
                curveTo(520.197f, 28.1573f, 516.699f, 26.7067f, 512.944f, 25.512f)
                curveTo(509.189f, 24.3173f, 505.563f, 23.72f, 502.064f, 23.72f)
                curveTo(497.285f, 23.72f, 493.488f, 24.8293f, 490.672f, 27.048f)
                curveTo(487.856f, 29.1813f, 486.448f, 32.1253f, 486.448f, 35.88f)
                curveTo(486.448f, 38.5253f, 487.387f, 40.8293f, 489.264f, 42.792f)
                curveTo(491.141f, 44.7547f, 493.573f, 46.4613f, 496.56f, 47.912f)
                curveTo(499.547f, 49.3627f, 502.704f, 50.728f, 506.032f, 52.008f)
                curveTo(508.848f, 53.032f, 511.621f, 54.2693f, 514.352f, 55.72f)
                curveTo(517.168f, 57.0853f, 519.685f, 58.792f, 521.904f, 60.84f)
                curveTo(524.208f, 62.888f, 526f, 65.448f, 527.28f, 68.52f)
                curveTo(528.645f, 71.5067f, 529.328f, 75.176f, 529.328f, 79.528f)
                curveTo(529.328f, 84.4773f, 528.091f, 88.9573f, 525.616f, 92.968f)
                curveTo(523.227f, 96.9787f, 519.728f, 100.179f, 515.12f, 102.568f)
                curveTo(510.597f, 104.872f, 505.093f, 106.024f, 498.608f, 106.024f)
                curveTo(494.683f, 106.024f, 490.843f, 105.597f, 487.088f, 104.744f)
                curveTo(483.419f, 103.805f, 479.92f, 102.611f, 476.592f, 101.16f)
                curveTo(473.349f, 99.624f, 470.405f, 97.96f, 467.76f, 96.168f)
                lineTo(473.904f, 85.544f)
                curveTo(475.952f, 86.9947f, 478.299f, 88.4027f, 480.944f, 89.768f)
                curveTo(483.589f, 91.048f, 486.32f, 92.072f, 489.136f, 92.84f)
                curveTo(492.037f, 93.608f, 494.768f, 93.992f, 497.328f, 93.992f)
                curveTo(500.144f, 93.992f, 502.917f, 93.5227f, 505.648f, 92.584f)
                curveTo(508.379f, 91.56f, 510.64f, 90.024f, 512.432f, 87.976f)
                curveTo(514.224f, 85.8427f, 515.12f, 83.0693f, 515.12f, 79.656f)
                curveTo(515.12f, 76.7547f, 514.309f, 74.3227f, 512.688f, 72.36f)
                curveTo(511.152f, 70.312f, 509.061f, 68.5627f, 506.416f, 67.112f)
                curveTo(503.856f, 65.6613f, 501.083f, 64.3813f, 498.096f, 63.272f)
                curveTo(495.195f, 62.1627f, 492.251f, 60.9253f, 489.264f, 59.56f)
                curveTo(486.277f, 58.1947f, 483.504f, 56.5733f, 480.944f, 54.696f)
                curveTo(478.384f, 52.7333f, 476.293f, 50.344f, 474.672f, 47.528f)
                curveTo(473.136f, 44.6267f, 472.368f, 41.0853f, 472.368f, 36.904f)
                curveTo(472.368f, 31.9547f, 473.52f, 27.6453f, 475.824f, 23.976f)
                curveTo(478.213f, 20.2213f, 481.499f, 17.2773f, 485.68f, 15.144f)
                curveTo(489.861f, 13.0107f, 494.683f, 11.816f, 500.144f, 11.56f)
                curveTo(506.373f, 11.56f, 511.792f, 12.328f, 516.4f, 13.864f)
                curveTo(521.008f, 15.4f, 525.104f, 17.2347f, 528.688f, 19.368f)
                lineTo(523.44f, 29.864f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFF2998FF)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(44.9999f, 16.6484f)
                lineTo(129f, 16.6484f)
            }
            path(
                fill = SolidColor(Color(0xFF2998FF)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(30f, 17.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.5f, 30f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 17.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30f, 17.5f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFFFFC93E)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(58.9999f, 60.4175f)
                lineTo(143f, 60.4175f)
            }
            path(
                fill = SolidColor(Color(0xFFFFC93E)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(44f, 61.5f)
                curveTo(44f, 68.4036f, 38.4036f, 74f, 31.5f, 74f)
                curveTo(24.5964f, 74f, 19f, 68.4036f, 19f, 61.5f)
                curveTo(19f, 54.5964f, 24.5964f, 49f, 31.5f, 49f)
                curveTo(38.4036f, 49f, 44f, 54.5964f, 44f, 61.5f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(Color(0xFFFF295D)),
                strokeAlpha = 1.0f,
                strokeLineWidth = 15f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(45f, 104.648f)
                lineTo(129f, 104.648f)
            }
            path(
                fill = SolidColor(Color(0xFFFF295D)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(30f, 105.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17.5f, 118f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 105.5f)
                arcTo(12.5f, 12.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 30f, 105.5f)
                close()
            }
        }.build()
    }
}