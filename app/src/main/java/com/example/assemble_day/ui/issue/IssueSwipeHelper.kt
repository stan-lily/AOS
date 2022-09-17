package com.example.assemble_day.ui.issue

import android.graphics.Canvas
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.assemble_day.R
import com.example.assemble_day.ui.common.eventListener.SwipeEventListener

class IssueSwipeHelper(private val swipeEventListener: SwipeEventListener) : ItemTouchHelper.Callback() {

    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val view = getView(viewHolder)
        // clamp 을 기준으로 swipe 됐는지 안됐는지를 판단
        // Issue 뷰 넓이의 30퍼 이상을 스와이프했다면 스와이프했다고 판단
        clamp = view.width.toFloat() * (0.3f)
        return if (isEditMode(viewHolder)) 0
        else makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        // 스와이프 속도에 따라 없어지는 기준 설정
        return defaultValue * 100
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // 현재 위치가 clamp 이상 갔을 때 스와이프 상태로 판단한다.
        // 스와이프 위치적으로 없어지는 기준 설정
        val issuePosition = viewHolder.adapterPosition

        if (currentDx <= -clamp) {
            swipeEventListener.clampItem(issuePosition)
        } else {
            swipeEventListener.unclampItem(issuePosition)
        }
        return 2f
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(getView(viewHolder))
    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = (viewHolder as IssueAdapter.IssueViewHolder).getBinding().issue?.isClamped ?: false

            val x = getClampHorizontalPosition(dX, isClamped, isCurrentlyActive)

            currentDx = x
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                x,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    private fun getClampHorizontalPosition(
        dX: Float,
        isClamped: Boolean,
        currentlyActive: Boolean
    ): Float {

        val right = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (currentlyActive) {
                -clamp + dX
            } else {
                -clamp
            }
            // 스와이프 된 고정 상태에서 dX - clamp 가 maxSwipe 이하인 상태에서 터치 유지 해제(왼쪽 스와이프 시도 중) -> maxSwipe 리턴
            // 스와이프 된 고정 상태에서 dx - clamp 가 maxSwipe 이상인 상태에서 터치 유지 해제(오른쪽 스와이프 시도 중) -> isClamped 에 false 값이 들어올 것임 -> currentDx가 -clamp 보다 커진다!
        } else {
            dX
        }
        // right = 항상 0 이므로  min 함수에서 최대는 항상 0 값이 나온다 -> 스와이프를 통해 오른쪽으로 밀리는 일은 없다!!
        return kotlin.math.min(kotlin.math.max(-clamp, x), right)
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        // 아이템뷰에서 스와이프 영역에 해당하는 뷰 가져오기
        return (viewHolder as IssueAdapter.IssueViewHolder).getBinding().clIssue
    }

    private fun isEditMode(viewHolder: RecyclerView.ViewHolder): Boolean {
        return (viewHolder as IssueAdapter.IssueViewHolder).getBinding().cbIssueSelector.isVisible
    }

}