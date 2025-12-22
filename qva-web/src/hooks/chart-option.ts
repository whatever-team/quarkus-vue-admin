import { computed } from 'vue';
import { EChartsOption } from 'echarts';
import { useAppStore } from '@/store';

type OptionsFn = (isDark: boolean) => EChartsOption | Record<string, any>;

export default function useChartOption(sourceOption: OptionsFn) {
  const appStore = useAppStore();
  const isDark = computed(() => {
    return appStore.theme === 'dark';
  });
  // echarts support https://echarts.apache.org/zh/theme-builder.html
  // It's not used here
  // TODO echarts themes
  const chartOption = computed<EChartsOption>(() => {
    return sourceOption(isDark.value) as EChartsOption;
  });
  return {
    chartOption,
  };
}

export type { OptionsFn };
